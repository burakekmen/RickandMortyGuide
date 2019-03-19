package com.burakekmen.rickandmortyguide.database

class FavouritesDB {

    val TABLE_NAME = "favourites"

    val COLUMN_ID = "id"
    val COLUMN_FAVOURITE = "characterId"

    private var id: Int = 0
    private var characterId: String? = null


    // Create table SQL query
    val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FAVOURITE + " TEXT"
                    + ")")

    fun FavouritesDB() {}

    fun FavouritesDB(id: Int, characterId: String) {
        this.id = id
        this.characterId = characterId
    }

    fun getCharacterId(): String? {
        return characterId
    }

}