package it.teutsch.felix.rant2text.data

import androidx.room.Database
import androidx.room.RoomDatabase
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.model.RantTableModel

@Database(entities = [RantTableModel::class], version = 1)
abstract class RantDatabase : RoomDatabase() {
    abstract val rantDao: RantDao
}