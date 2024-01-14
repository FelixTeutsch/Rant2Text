package it.teutsch.felix.text2rant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import it.teutsch.felix.text2rant.data.dao.RantDao
import it.teutsch.felix.text2rant.data.model.RantTableModel

@Database(entities = [RantTableModel::class], version = 1)
abstract class Rent2TextDatabase : RoomDatabase() {
    abstract val rantDao: RantDao
}