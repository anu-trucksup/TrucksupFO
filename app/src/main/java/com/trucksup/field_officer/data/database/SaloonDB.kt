package com.trucksup.field_officer.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.trucksup.field_officer.data.database.dao.SaloonDAO
import com.trucksup.field_officer.data.database.model.TranslationTableModel

@Database(entities = [TranslationTableModel::class],  version = 1)
abstract class SaloonDB: RoomDatabase() {
    abstract fun getSaloonDao(): SaloonDAO
}

