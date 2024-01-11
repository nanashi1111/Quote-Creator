package com.klmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.klmobile.data.dto.QuoteCategoryDTO
import com.klmobile.data.dto.QuoteDTO

@Database(entities = [QuoteDTO::class, QuoteCategoryDTO::class], version = 2)
abstract class QuoteDatabase: RoomDatabase() {
  abstract fun quoteDao(): QuoteDao
}