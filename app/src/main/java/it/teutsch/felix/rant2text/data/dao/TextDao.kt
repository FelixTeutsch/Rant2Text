package it.teutsch.felix.rant2text.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.teutsch.felix.rant2text.data.model.TextTableModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertText(text: TextTableModel): Long

    @Update
    suspend fun updateText(text: TextTableModel)

    @Delete
    suspend fun deleteText(text: TextTableModel)

    @Query("SELECT * FROM texts WHERE rantId = :rantId ORDER BY textId DESC")
    fun getTextsByRantId(rantId: Int): Flow<List<TextTableModel>>
}