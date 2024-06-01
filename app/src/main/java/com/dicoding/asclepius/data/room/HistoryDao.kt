package com.dicoding.asclepius.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.data.entity.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History ORDER BY id ASC")
    fun get(): LiveData<List<History>>

    @Query("SELECT * FROM History WHERE id = :id")
    fun find(id: Int): LiveData<History>

    @Insert
    fun insert(history: History)

}