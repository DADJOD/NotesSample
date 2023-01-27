package com.example.notessample

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper(mContext: Context) :
    SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DB_CREATE)
        // TODO DELETE AFTER DEBUGGING
        val values = ContentValues(1)
        for (i in 0..50) {
            values.put(COLUMN_NOTE, "NOTE # $i")
            db.insert(DB_TABLE, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        const val DB_NAME = "notes.db"
        const val DB_VERSION = 1
        const val DB_TABLE = "notes"
        const val COLUMN_NOTE = "note"
        private const val DB_CREATE = "CREATE TABLE $DB_TABLE " +
                    "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NOTE TEXT NOT NULL) ;"
    }
}

//class DbOpenHelper(mContext: Context) :
//    SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL(DB_CREATE)
//        // TODO DELETE AFTER DEBUGGING
//        val values = ContentValues(1)
//        for (i in 0..4) {
//            values.put(COLUMN_NOTE, "Note #$i")
//            db.insert(DB_TABLE, null, values)
//        }
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
//
//    companion object {
//        const val DB_NAME = "notes.db"
//        const val DB_TABLE = "notes"
//        const val COLUMN_NOTE = "note"
//        const val DB_VERSION = 1
//        private const val DB_CREATE = ("CREATE TABLE "
//                + DB_TABLE + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_NOTE + " TEXT NOT NULL);")
//    }
//}