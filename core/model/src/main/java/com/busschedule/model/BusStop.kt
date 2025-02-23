package com.busschedule.model

import kotlinx.serialization.Serializable

@Serializable
data class BusStop(
    val region: String = "",
    val busStop: String = "",
    val nodeId: String = "",
)
