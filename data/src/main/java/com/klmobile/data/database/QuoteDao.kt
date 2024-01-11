package com.klmobile.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.klmobile.data.dto.QuoteCategoryDTO
import com.klmobile.data.dto.QuoteDTO

@Dao
interface QuoteDao {
  @Query("Select * from QuoteCategoryDTO")
  fun getCategories(): List<QuoteCategoryDTO>

  @Query("Select * from QuoteDTO where Category = :category")
  fun getQuotesFromCategory(category: String): List<QuoteDTO>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addCategories(categories: List<QuoteCategoryDTO>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addQuotes(quotes: List<QuoteDTO>)
}