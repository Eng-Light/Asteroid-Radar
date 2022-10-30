package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.models.DataBaseNasaImage
import com.udacity.asteroidradar.database.models.DatabaseAsteroid

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: DataBaseNasaImage)

    @Query("SELECT * from daily_nasa_image")
    fun getNasaImage(): LiveData<DataBaseNasaImage>

    @Query(
        "SELECT * FROM daily_asteroid_table " +
                "WHERE close_approach_date >= :today " +
                "ORDER BY close_approach_date ASC"
    )
    fun getSortedCurrentWeekAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query(
        "SELECT * FROM daily_asteroid_table " +
                "WHERE close_approach_date = :today"
    )
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM daily_asteroid_table ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM daily_asteroid_table WHERE close_approach_date < :startDate")
    suspend fun deleteAsteroidBefore(startDate: String)
}