package ru.skillbranch.gameofthrones.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import ru.skillbranch.gameofthrones.data.local.entities.House

@Dao
interface HouseDao {
    @Insert
    fun insertHouses(houses: List<House>)

    @Query("SELECT COUNT(*) FROM houses")
    fun getCountEntity(): Single<Int>
}