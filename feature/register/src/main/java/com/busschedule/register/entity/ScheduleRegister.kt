package com.busschedule.register.entity

import com.busschedule.model.BusStop
import com.busschedule.util.entity.DayOfWeekUi

data class ScheduleRegister (
    val name: String = "",
    val dayOfWeeks: List<DayOfWeekUi> = emptyList(),
    val startTime: String = "",
    val endTime: String = "",
    val isNotify: Boolean = false,
    val routeInfos: List<BusStopInfoUI> = emptyList(),
    val arriveBusStop: BusStop = BusStop(),
)
