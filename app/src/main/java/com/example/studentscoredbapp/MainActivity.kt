package com.example.studentscoredbapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.database.Cursor
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAddScore = findViewById<Button>(R.id.buttonAdd)

        btnAddScore.setOnClickListener {
            val db = DBHelper(this, null)
            val etSubject = findViewById<EditText>(R.id.editTextSubject)
            val etScore = findViewById<EditText>(R.id.editTextScore)
            val subject = etSubject.text.toString()
            val score = etScore.text.toString()
            db.addUser(subject, score)

            Toast.makeText(this, subject + " added to database", Toast.LENGTH_SHORT).show()
            etSubject.text.clear()
            etScore.text.clear()
        }
        val btnPrintUsers = findViewById<Button>(R.id.buttonPrint)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val subjectList = db.getAllUsers()

            val tvUserRecord = findViewById<TextView>(R.id.textViewRecords)
            tvUserRecord.text = "### Users ###\n"

            subjectList.forEach{
                tvUserRecord.append("$it\n")
            }
        }
        val btnDeleteUser = findViewById<Button>(R.id.buttonDelete)
        btnDeleteUser.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = findViewById<EditText>(R.id.editTextSubject).text.toString()
            val rows = db.deleteUser(subject)

            Toast.makeText(
                this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 user deleted"
                    else -> "" // shouldn't happen
                },
                Toast.LENGTH_LONG).show()
        }
        val btnUpdateUser = findViewById<Button>(R.id.buttonUpdate)
        btnUpdateUser.setOnClickListener {
            val db = DBHelper(this, null)
            val subject = findViewById<EditText>(R.id.editTextSubject).text.toString()
            val score = findViewById<EditText>(R.id.editTextScore).text.toString()
            val rows = db.updateUser(subject, score)
            Toast.makeText(this, "$rows users updated", Toast.LENGTH_LONG).show()
        }

        val btnDeleteDB = findViewById<Button>(R.id.buttonDelDB)
        btnDeleteDB.setOnClickListener {
            val db = DBHelper(this, null)
            val isSuccessful = db.deleteDB()
            Toast.makeText(this,
                when (isSuccessful) {
                    true -> "Database successfully deleted"
                    false -> "Failed to delete database"
                },
                Toast.LENGTH_LONG
            ).show()
        }
    }
}