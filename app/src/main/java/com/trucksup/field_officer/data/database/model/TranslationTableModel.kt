package com.trucksup.field_officer.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations_table")
class TranslationTableModel (
    @PrimaryKey
    @ColumnInfo(name = "languageCode")
    var languageCode: String,
    @ColumnInfo(name = "translations")
    var translations: String?

)