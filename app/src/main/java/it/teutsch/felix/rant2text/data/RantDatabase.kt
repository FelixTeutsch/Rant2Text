package it.teutsch.felix.rant2text.data

import androidx.room.Database
import androidx.room.RoomDatabase
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.dao.TextDao
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.data.model.TextTableModel

@Database(entities = [RantTableModel::class, TextTableModel::class], version = 2)
abstract class RantDatabase : RoomDatabase() {
    abstract val rantDao: RantDao
    abstract val textDao: TextDao
}