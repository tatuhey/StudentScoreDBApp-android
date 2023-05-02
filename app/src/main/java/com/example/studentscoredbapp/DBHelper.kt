package com.example.studentscoredbapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.studentscoredbapp.Subject

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    var context: Context
    init {
        this.context = context
    }

    companion object {
        private val DB_NAME = "smt"
        private val DB_VERSION = 1
        val TABLE_NAME = "user_table"
        val ID = "id"
        val SUBJECT = "subject"
        val SCORE = "score"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$SUBJECT TEXT," +
                        "$SCORE TEXT" + ")"
                )
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(subject: String, score: String) {
        var db = this.writableDatabase
        val values = ContentValues()

        db.beginTransaction()
        try{
            values.put(SUBJECT, subject)
            values.put(SCORE, score)

            val db = this.writableDatabase

            db.insert(TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        }
        finally {
            db.endTransaction()
        }

        db.close()
    }

    fun getAllUsers(): ArrayList<Subject> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        val subjectList = ArrayList<Subject>() // User ArrayList
        if (cursor.moveToFirst()) {
            do { // add all users to the list
                subjectList.add(
                    Subject(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SUBJECT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SCORE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return subjectList
    }
    fun deleteUser(subject: String): Int {
        val db = this.writableDatabase
        var rows: Int
        db.beginTransaction()
        try {
            rows = db.delete(TABLE_NAME, "subject=?", arrayOf(subject));

            db.setTransactionSuccessful()
        }finally {
            db.endTransaction()
        }

        return rows
        db.close()

    }
    fun updateUser(subject: String, score: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        var rows: Int
        db.beginTransaction()
        try {
            values.put(SCORE, score)
            rows = db.update(TABLE_NAME, values, "subject=?", arrayOf(subject))

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return rows
        db.close()
    }

    fun deleteDB(): Boolean{
        return context.deleteDatabase(DB_NAME)
    }
    // This method is to recreated DB and tables
    fun recreateDatabaseAndTables() {
    }
}