package it.teutsch.felix.rant2text.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.teutsch.felix.rant2text.data.model.RantTableModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RantDao {
    @Insert
    suspend fun insertRant(rant: RantTableModel): Long

    @Update
    suspend fun updateRant(rant: RantTableModel)

    @Delete
    suspend fun deleteRant(rant: RantTableModel)

    @Query("SELECT * FROM rants")
    fun getRants(): Flow<List<RantTableModel>>


    @Query("SELECT * FROM rants WHERE id=(:rantId)")
    fun getRantById(rantId: Int): Flow<RantTableModel>
}