package com.example.notessample

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val fields = arrayOf("_id", DbOpenHelper.COLUMN_NOTE)
    private val from   = arrayOf(DbOpenHelper.COLUMN_NOTE)
    private val to     = intArrayOf(R.id.noteView)
    private val order  = "_id DESC"

    private lateinit var mInputField: EditText
    private lateinit var mNotesList: ListView

    private var mHelper = DbOpenHelper(this)
    private lateinit var mDb: SQLiteDatabase

    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mInputField = findViewById(R.id.inputField)
        mNotesList  = findViewById(R.id.notesList)
        mAdapter = SimpleCursorAdapter(
            this, R.layout.note,
            null, from, to, 0)
        mNotesList.adapter = mAdapter //!!
    }

    override fun onResume() {
        super.onResume()
        showNotes()
    }

    private fun showNotes() {
        mDb = mHelper.writableDatabase //открывашка DataBase
        val c = mDb.query(
            DbOpenHelper.DB_TABLE, fields, null, null,
            null, null, order
        )
        mAdapter.swapCursor(c)
    }

    override fun onStop() {
        super.onStop()
        mDb.close()
    }

    fun onOkButtonClick(view: View) {
        val newNote = mInputField.text.toString().trim()
        if (newNote.isNotEmpty()) {
            val values = ContentValues(1)
            values.put(DbOpenHelper.COLUMN_NOTE, newNote)
            mDb = mHelper.writableDatabase //открывашка DataBase
            mDb.insert(DbOpenHelper.DB_TABLE, null, values)
            showNotes()
        }
        mInputField.text = null
    }
}