package com.example.notekeeperapp

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var notes: MutableList<Note>
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val notesListView: ListView = findViewById(R.id.notesContainer)
        db = DatabaseHelper(this)
        notes = db.readNotes()

        noteAdapter = NoteAdapter(this, notes)
        notesListView.adapter = noteAdapter

        notesListView.setOnItemClickListener { parent, view, position, id ->
            val clickedNote = notes[position]
            val intent = Intent(this, UpdateNoteActivity::class.java)
            intent.putExtra("note_id", clickedNote.id)
            startActivity(intent)
        }

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        notes.clear()
        notes.addAll(db.readNotes())
        noteAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}