package com.klmobile.data.dto

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.klmobile.data.QuotesSource

@Keep
@Entity
data class QuoteCategoryDTO (@PrimaryKey val id: Long = QuotesSource.randomId(), @ColumnInfo("CategoryName") val categoryName: String)