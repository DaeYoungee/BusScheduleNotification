package com.busschedule.util.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class DayOfWeekUi(
    val dayOfWeek: DayOfWeek,
    private val init: Boolean = false
) {
    var isSelected by mutableStateOf(init)

    fun updateSelected(selected: Boolean) = apply {
        isSelected = selected
    }

    fun getDayOfWeeks() = "${dayOfWeek.value}요일"
}
