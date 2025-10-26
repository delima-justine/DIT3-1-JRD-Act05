package com.example.notekeeperapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DATABASE_NAME = "db_note_app.db"
private val DATABASE_VERSION = 2
private val TABLE_NAME = "tbl_note"
private val ID_COLUMN = "id"
private val TITLE_COLUMN = "title"
private val CONTENT_COLUMN = "content"
private val TIMESTAMP_COLUMN = "timestamp"

class DatabaseHelper(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                                ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                TITLE_COLUMN + " TEXT, " +
                                CONTENT_COLUMN + " TEXT, " +
                                TIMESTAMP_COLUMN + " TEXT NOT NULL); "

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

    fun insertNote(note: Note): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE_COLUMN, note.title)
        contentValues.put(CONTENT_COLUMN, note.content)
        contentValues.put(TIMESTAMP_COLUMN, note.timestamp)

        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Range")
    fun readNotes(): MutableList<Note> {
        val list: MutableList<Note> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $ID_COLUMN DESC"
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                val id = result.getInt(result.getColumnIndex(ID_COLUMN))
                val title = result.getString(result.getColumnIndex(TITLE_COLUMN))
                val content = result.getString(result.getColumnIndex(CONTENT_COLUMN))
                val timestamp = result.getString(result.getColumnIndex(TIMESTAMP_COLUMN))
                val note = Note(id, title, content, timestamp)
                list.add(note)
            } while(result.moveToNext())
        }
        result.close()
        return list
    }

    @SuppressLint("Range")
    fun getNoteById(noteId: Int): Note? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID_COLUMN = $noteId"
        val cursor = db.rawQuery(query, null)
        var note: Note? = null

        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN))
            val title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN))
            val content = cursor.getString(cursor.getColumnIndex(CONTENT_COLUMN))
            val timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP_COLUMN))
            note = Note(id, title, content, timestamp)
        }
        cursor.close()
        return note
    }

    fun updateNote(note: Note): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE_COLUMN, note.title)
        contentValues.put(CONTENT_COLUMN, note.content)

        val whereClause = "$ID_COLUMN = ?"
        val whereArgs = arrayOf(note.id.toString())

        val result = db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
        return result > 0
    }

    fun deleteNote(noteId: Int): Boolean {
        val db = this.writableDatabase
        val whereClause = "$ID_COLUMN = ?"
        val whereArgs = arrayOf(noteId.toString())

        val result = db.delete(TABLE_NAME, whereClause, whereArgs)
        return result > 0
    }
}