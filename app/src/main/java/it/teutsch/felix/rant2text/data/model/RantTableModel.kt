package it.teutsch.felix.rant2text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel

@Entity(tableName = "rants")
data class RantTableModel(
    val title: String = "No Title",
    val text: String = "No Text",
    val angerLevel: EAngerLevel = EAngerLevel.None,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)