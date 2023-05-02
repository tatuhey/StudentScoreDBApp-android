package com.example.studentscoredbapp


class Subject (val id: Int, val subject:String , val score: String) {
    override fun toString(): String {
        return "$id: $subject($score)"
    }
}