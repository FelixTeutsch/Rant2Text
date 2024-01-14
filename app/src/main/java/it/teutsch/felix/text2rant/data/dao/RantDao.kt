package it.teutsch.felix.text2rant.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.teutsch.felix.text2rant.data.model.RantTableModel
import kotlinx.coroutines.flow.Flow

interface RantDao {
    @Insert
    suspend fun insertRant(rant: RantTableModel)

    @Update
    suspend fun updateRant(rant: RantTableModel)

    @Delete
    suspend fun deleteRant(rant: RantTableModel)

    @Query("SELECT * FROM rants")
    fun getRants(): Flow<List<RantTableModel>>
}