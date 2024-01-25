package it.teutsch.felix.rant2text.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.teutsch.felix.rant2text.data.model.RantTextTableModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertText(text: RantTextTableModel): Long

    @Update
    suspend fun updateText(text: RantTextTableModel)

    @Delete
    suspend fun deleteText(text: RantTextTableModel)

    @Query("SELECT * FROM texts WHERE rantId = :rantId ORDER BY id DESC")
    fun getTextsByRantId(rantId: Int): Flow<List<RantTextTableModel>>

    @Query("SELECT * FROM texts WHERE rantId = :rantId ORDER BY id DESC LIMIT 2")
    suspend fun getLastTwoMessagesByRantId(rantId: Int): List<RantTextTableModel>?
}