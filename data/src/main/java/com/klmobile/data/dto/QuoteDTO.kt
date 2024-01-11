package com.klmobile.data.dto

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klmobile.data.QuotesSource

@Keep
@Entity
data class QuoteDTO(@PrimaryKey val id: Long = QuotesSource.randomId(), @ColumnInfo("Category") val category: String, @ColumnInfo("Content") val content: String)