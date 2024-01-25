package it.teutsch.felix.rant2text.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "texts",
    foreignKeys = [
        ForeignKey(
            entity = RantListTableModel::class,
            parentColumns = ["id"],
            childColumns = ["rantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RantTextTableModel(
    val rantId: Int = 0,
    var text: String = "",
    val date: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
