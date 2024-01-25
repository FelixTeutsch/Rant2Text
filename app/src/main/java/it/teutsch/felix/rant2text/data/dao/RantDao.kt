package it.teutsch.felix.rant2text.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RantDao {
    @Insert
    suspend fun insertRant(rant: RantListTableModel): Long

    @Update
    suspend fun updateRant(rant: RantListTableModel)

    @Delete
    suspend fun deleteRant(rant: RantListTableModel)

    @Query("SELECT * FROM rants")
    fun getRants(): Flow<List<RantListTableModel>>


    @Query("SELECT * FROM rants WHERE id=(:rantId)")
    fun getRantById(rantId: Int): Flow<RantListTableModel>

    @Query("SELECT * FROM rants ORDER BY charCount DESC LIMIT 1")
    fun getRantWithMostChars(): Flow<RantListTableModel>

    @Query("SELECT * FROM rants ORDER BY charCount ASC LIMIT 1")
    fun getRantWithLeastChars(): Flow<RantListTableModel>

}