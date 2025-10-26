package com.example.notekeeperapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context, private val notes: MutableList<Note>) : ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val note = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false)
        }

        val titleTextView = view!!.findViewById<TextView>(R.id.titleOutput)
        val contentTextView = view.findViewById<TextView>(R.id.contentOutput)

        titleTextView.text = note?.title
        contentTextView.text = note?.content

        return view
    }
}