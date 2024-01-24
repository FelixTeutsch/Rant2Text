package it.teutsch.felix.rant2text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel

@Entity(tableName = "rants")
data class RantTableModel(
    //TODO: turn em back to vals after the chat page is settled
    var title: String = "",
    var text: String = "",
    val angerLevel: EAngerLevel = EAngerLevel.None,
    val date: Long = System.currentTimeMillis(),
    var wordCount: Int = 0,
    var charCount: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)