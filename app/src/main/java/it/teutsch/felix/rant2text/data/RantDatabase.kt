package it.teutsch.felix.rant2text.data

import androidx.room.Database
import androidx.room.RoomDatabase
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.dao.TextDao
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.data.model.RantTextTableModel

@Database(entities = [RantListTableModel::class, RantTextTableModel::class], version = 2)
abstract class RantDatabase : RoomDatabase() {
    abstract val rantDao: RantDao
    abstract val textDao: TextDao
}