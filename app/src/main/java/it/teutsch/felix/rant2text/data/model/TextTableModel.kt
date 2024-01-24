package it.teutsch.felix.rant2text.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "texts",
    foreignKeys = [
        ForeignKey(
            entity = RantTableModel::class,
            parentColumns = ["id"],
            childColumns = ["rantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TextTableModel(
    @PrimaryKey(autoGenerate = true)
    val textId: Int = 0,
    val rantId: Int,
    var text: String = "",
    val date: Long = System.currentTimeMillis(),
)
