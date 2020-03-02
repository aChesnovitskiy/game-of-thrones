package ru.skillbranch.gameofthrones.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.House

@Database(
    entities = [Character::class, House::class],
    version = 1,
    exportSchema = false
)
abstract class GoTDatabase : RoomDatabase() {
    abstract val houseDao: HouseDao
    abstract val characterDao: CharacterDao

    companion object {
        private const val DB_NAME = "GoT.db"
        private val LOCK = Any() // Block key

        private var instance: GoTDatabase? = null

        fun getInstance(context: Context): GoTDatabase? {
            if (instance == null) {
                synchronized(LOCK) {
                    instance = Room.databaseBuilder(context, GoTDatabase::class.java, DB_NAME).build()
                }
            }
            return instance
        }
    }
}