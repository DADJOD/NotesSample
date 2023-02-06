package com.example.notessample

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
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
    private var mNoteId = -1
    private lateinit var mOldNote: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mInputField = findViewById(R.id.inputField)
        mNotesList  = findViewById(R.id.notesList)
        mAdapter = SimpleCursorAdapter(
            this, R.layout.note,
            null, from, to, 0)
        mNotesList.adapter = mAdapter
        registerForContextMenu(mNotesList)
        registerForContextMenu(mInputField)
    }

    // redrawing
    override fun onResume() {
        super.onResume()
        showNotes()
    }

    // freeing up resources
    override fun onStop() {
        super.onStop()
        mDb.close()
    }

    private fun showNotes() {
        mDb = mHelper.writableDatabase // open DataBase
        val c = mDb.query(
            DbOpenHelper.DB_TABLE, fields, null, null,
            null, null, order
        )
        mAdapter.swapCursor(c) // show your items from list
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun onOkButtonClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val note = mInputField.text.toString().trim()

        if (note.isNotEmpty()) {
            val values = ContentValues(1)
            values.put(DbOpenHelper.COLUMN_NOTE, note)
            mDb = mHelper.writableDatabase // open DataBase

            if (mNoteId >= 0) {
                mDb.update(DbOpenHelper.DB_TABLE, values,
                    "_id = $mNoteId", null)
            } else {
                mDb.insert(DbOpenHelper.DB_TABLE, null, values)
            }
            showNotes()
        }
        mNoteId = -1
        mOldNote = null.toString()
        mInputField.text = null
    }

    // create context menu
    override fun onCreateContextMenu(menu: ContextMenu?,
                                     v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        when (v?.id) {
            R.id.notesList ->
                menuInflater.inflate(R.menu.notes_menu, menu)
        }
    }

    // select item from context menu
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_edit -> {
                val info: AdapterView.AdapterContextMenuInfo = // теперь через info можно получить item через .id. Через Adapter он имеет доступ к данным noteList
                    item.menuInfo as AdapterView.AdapterContextMenuInfo
                mOldNote = getNoteById(info.id).toString() // save for future use
                mInputField.setText(mOldNote)              // fill in input field
                mNoteId = info.id.toInt()                  // save for future use
                true
            }
            R.id.item_delete -> {
                val info: AdapterView.AdapterContextMenuInfo = // теперь через info можно получить item через .id. Через Adapter он имеет доступ к данным noteList
                    item.menuInfo as AdapterView.AdapterContextMenuInfo
                deleteNote(info.id)
                showNotes()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // return note by his id
    private fun getNoteById(id: Long): String? {
        val c = mDb.query(DbOpenHelper.DB_TABLE, fields,"_id = $id",
            null, null, null, null)

        var note: String? = null
        if (c != null) {
            c.moveToFirst()     // move Cursor to first
            note = c.getString(
                c.getColumnIndexOrThrow(DbOpenHelper.COLUMN_NOTE)) // get item which you choose in context menu
            c.close()
        }
        return note
    }

    private fun deleteNote(id: Long) {
        mDb.delete(DbOpenHelper.DB_TABLE, "_id = $id", null)
    }
}