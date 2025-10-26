package com.example.notekeeperapp

import android.content.Intent
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewNoteActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_note)

        db = DatabaseHelper(this)

        val backBtn: FloatingActionButton = findViewById<FloatingActionButton>(R.id.backBtn)
        val addNoteBtn: Button = findViewById<Button>(R.id.addNoteBtn)
        val titleInput = findViewById<EditText>(R.id.titleInput)
        val contentInput = findViewById<EditText>(R.id.contentInput)
        val timestampContainer = findViewById<TextView>(R.id.timestampContainer)

        // Get current time for the timestamp (API 25 compatible)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timestamp = formatter.format(Date())

        timestampContainer.text = timestamp

        // Returns to main page
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Add Note to DB
        addNoteBtn.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()

            val note = Note(title, content, timestamp)
            if (db.insertNote(note)) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Unsuccessful operation", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}