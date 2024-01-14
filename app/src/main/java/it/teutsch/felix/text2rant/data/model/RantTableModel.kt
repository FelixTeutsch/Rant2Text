package it.teutsch.felix.text2rant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.teutsch.felix.text2rant.ui.enumeration.EAngerLevel

@Entity(tableName = "rants")
data class RantTableModel(
    val title: String,
    val text: String,
    val angerLevel: EAngerLevel,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)