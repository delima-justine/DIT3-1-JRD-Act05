package com.example.notekeeperapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_note)

        db = DatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        val titleInput = findViewById<EditText>(R.id.updateTitleInput)
        val contentInput = findViewById<EditText>(R.id.updateContentInput)
        val updateButton = findViewById<Button>(R.id.updateNoteBtn)
        val deleteButton = findViewById<Button>(R.id.deleteNoteBtn)
        val timestampContainer = findViewById<TextView>(R.id.newTimestampContainer)

        // Display the original timestamp from the note
        timestampContainer.text = note?.timestamp

        titleInput.setText(note?.title)
        contentInput.setText(note?.content)

        updateButton.setOnClickListener {
            val newTitle = titleInput.text.toString()
            val newContent = contentInput.text.toString()
            // When updating, we keep the original timestamp
            val updatedNote = Note(noteId, newTitle, newContent, note?.timestamp ?: "")
            if (db.updateNote(updatedNote)) {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                finish() // Go back to MainActivity
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            deleteNote()
        }

        val backBtn: FloatingActionButton = findViewById<FloatingActionButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun deleteNote() {
        if (noteId == -1) {
            return
        }
        if (db.deleteNote(noteId)) {
            Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
            finish() // Go back to MainActivity
        } else {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}