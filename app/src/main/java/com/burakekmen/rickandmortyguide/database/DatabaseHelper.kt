package com.burakekmen.rickandmortyguide.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context!!, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_FAVOURITES " +
                "($ID Integer PRIMARY KEY, $CHARACTER_ID TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }


    fun isHaveFavouriteCharacter(characterId: String): Boolean {
        var sonuc = false

        val liste = getAllFavourites()

        if (liste.contains(characterId.toInt()))
            sonuc = true

        return sonuc
    }

    //Inserting (Creating) data
    fun addFavourite(characterId: String): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CHARACTER_ID, characterId)

        val sonuc = db.insert(TABLE_FAVOURITES, null, values)
        db.close()
        return (Integer.parseInt("$sonuc") != -1)
    }


    fun removeFavourite(characterId: String): Boolean {
        val db = this.writableDatabase
        var sonuc = 0
        sonuc = try {
            // id ye göre verimizi siliyoruz
            val where = "$CHARACTER_ID = $characterId"
            db.delete(TABLE_FAVOURITES, where, null)
        } catch (e: Exception) {
            0
        }

        db.close()

        return (Integer.parseInt("$sonuc") != -1)
    }

    //get all users
    fun getAllFavourites(): MutableList<Int> {
        val favourites = mutableListOf<Int>()
        val db = readableDatabase
        val sutunlar = arrayOf<String>(CHARACTER_ID)
        val cursor = db.query(TABLE_FAVOURITES, sutunlar, null, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                favourites.add(cursor.getInt(0))
            }
        }
        cursor.close()

        db.close()
        return favourites
    }

    companion object {
        private const val DB_NAME = "RaMDB"
        private const val DB_VERSIOM = 1
        private const val TABLE_FAVOURITES = "favourites"
        private const val ID = "id"
        private const val CHARACTER_ID = "characterId"
    }
}