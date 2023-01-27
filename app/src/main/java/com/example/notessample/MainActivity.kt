package com.example.notessample

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
        mAdapter    = SimpleCursorAdapter(
            this, R.layout.note,
            null, from, to, 0)
        mNotesList.adapter = mAdapter //!!
    }

    override fun onResume() {
        super.onResume()
        mDb = mHelper.writableDatabase
        val c = mDb.query(
            DbOpenHelper.DB_TABLE, fields, null, null,
            null, null, order)

        mAdapter.swapCursor(c)
    }

    fun onOkButtonClick(view: View) {}

//    private val FIELDS: Array<String?>? = arrayOf<String>("_id", DbOpenHelper.COLUMN_NOTE)
//    private val FROM = arrayOf(DbOpenHelper.COLUMN_NOTE)
//    private val TO = intArrayOf(R.id.noteView)
//    private val ORDER = "_id DESC"
//
//    var mInputField: EditText? = null
//    var mNotesList: ListView? = null
//
//    var mHelper = DbOpenHelper(this)
//    var mDb: SQLiteDatabase? = null
//
//    var mAdapter: SimpleCursorAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        mInputField = findViewById<View>(R.id.inputField) as EditText
//        mNotesList = findViewById<View>(R.id.notesList) as ListView
//        mAdapter = SimpleCursorAdapter(
//            this, R.layout.note,
//            n ull, FROM, TO, 0
//        )
//        mNotesList!!.adapter = mAdapter
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mDb = mHelper.writableDatabase
//        val c: Cursor = mDb.query(
//            DbOpenHelper.DB_TABLE, FIELDS,
//            null, null, null, null, ORDER
//        )
//        mAdapter!!.swapCursor(c)
//    }
//
//    fun onOkButtonClick(view: View?) {}
}