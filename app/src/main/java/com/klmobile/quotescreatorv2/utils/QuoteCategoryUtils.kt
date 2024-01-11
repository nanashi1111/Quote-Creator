package com.klmobile.quotescreatorv2.utils

import com.klmobile.domain.entities.QuoteCategoryView
import com.klmobile.quotescreatorv2.R

 val quoteBackgroundList = listOf<Int>(
   R.drawable.bg_01,
   R.drawable.bg_02,
   R.drawable.bg_03,
   R.drawable.bg_04,
   R.drawable.bg_05,
   R.drawable.bg_06,
   R.drawable.bg_07,
   R.drawable.bg_08,
   R.drawable.bg_09,
   R.drawable.bg_10,
   R.drawable.bg_11,
   R.drawable.bg_12,
   R.drawable.bg_13,
   R.drawable.bg_14,
   R.drawable.bg_15,
   R.drawable.bg_16,
   R.drawable.bg_17,
   R.drawable.bg_18,
   R.drawable.bg_19,
   R.drawable.bg_20,
   R.drawable.bg_21,
   R.drawable.bg_22,
   R.drawable.bg_23,
   R.drawable.bg_24,
   R.drawable.bg_25,
   R.drawable.bg_26,
   R.drawable.bg_27,
   R.drawable.bg_28,
   R.drawable.bg_29,
   R.drawable.bg_30,
   R.drawable.bg_31,
 )
fun QuoteCategoryView.getBackgroundImage(): Int {
  return when (categoryName) {
    "Love" -> R.drawable.bg_category_love
    "Friendship" -> R.drawable.bg_category_friendship
    "Motivation" -> R.drawable.bg_category_motivation
    "Family" -> R.drawable.bg_category_family
    "Life" -> R.drawable.bg_category_life
    "Positive" -> R.drawable.bg_category_positive
    "Happiness" -> R.drawable.bg_category_happiness
    "Father" -> R.drawable.bg_category_father
    "Mother" -> R.drawable.bg_category_mother
    "Trust" -> R.drawable.bg_category_trust
    "Alone" -> R.drawable.bg_category_alone
    "Sad" -> R.drawable.bg_category_sad
    "Funny" -> R.drawable.bg_category_funny
    "Women" -> R.drawable.bg_category_woman
    else -> R.drawable.bg_category_woman
  }
}
