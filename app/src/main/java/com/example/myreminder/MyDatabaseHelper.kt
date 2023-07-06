package com.example.myreminder

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class MyDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createtablequery =
            "CREATE TABLE IF NOT EXISTS my_table1 (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,Discription TEXT,mnth TEXT,days TEXT,hour TEXT,min TEXT)"
        db.execSQL(createtablequery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun dropTable(tablename: String) {
        val db = this.writableDatabase
        val droptableqry = "DROP TABLE IF EXISTS $tablename"
        db.execSQL(droptableqry)
    }

    fun createTable(tablename: String) {
        val db = this.writableDatabase
        val createtablequery =
            "CREATE TABLE IF NOT EXISTS $tablename(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,mnth TEXT,days TEXT,hour TEXT,min TEXT)"
        db.execSQL(createtablequery)
    }

    fun insertData(name: String?,Discription: String?, mnth: String?, day: String?, hour: String?, min: String?) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("Discription",Discription)
        values.put("mnth", mnth)
        values.put("days", day)
        values.put("hour", hour)
        values.put("min", min)
        db.insert("my_table1", null, values)
    }

    fun updateValues(id: Int,name: String?,Discription: String?, mnth: String?, day: String?, hour: String?, min: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("Discription", Discription)
        contentValues.put("mnth", mnth)
        contentValues.put("days", day)
        contentValues.put("hour", hour)
        contentValues.put("min", min)
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())
        db.update("my_table1", contentValues, whereClause, whereArgs)
        db.close()
    }

    fun deleteRow(id: Int) {
        val db = this.writableDatabase
        val selection = "id=?"
//        val selection ="name = ? AND mnth = ? AND days = ?  AND hour = ? AND min = ?  " // Selection criteria
//        val selectionArgs = arrayOf(name, mnth, days, hour, min)
        db.delete("my_table1", selection, /*selectionArgs*/arrayOf(id.toString()))
        db.close()
    }
    fun getUsers(): ArrayList<Item> {
        val userList = ArrayList<Item>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM my_table1", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val Discription = cursor.getString(cursor.getColumnIndexOrThrow("Discription"))
                val mnth = cursor.getString(cursor.getColumnIndexOrThrow("mnth"))
                val days = cursor.getString(cursor.getColumnIndexOrThrow("days"))
                val hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"))
                val min = cursor.getString(cursor.getColumnIndexOrThrow("min"))

                val user = Item(id,name,Discription,mnth, days, hour, min)
                userList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return userList
    }


    companion object {
        private const val DATABASE_NAME = "eventslist.db"
        private const val DATABASE_VERSION = 1
    }
}
