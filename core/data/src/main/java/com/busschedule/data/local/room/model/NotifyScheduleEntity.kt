package com.busschedule.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.busschedule.model.BusStopInfo
import com.busschedule.model.NotifySchedule


@Entity(tableName = EntityTable.NOTIFY_SCHEDULE)
data class NotifyScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scheduleId: String,
    val scheduleName: String,
    val busStopIndex: Int = 0,
    val busStopInfos: List<BusStopInfo> = emptyList()
)

fun NotifyScheduleEntity.toModel() = NotifySchedule(
    scheduleId = scheduleId,
    scheduleName = scheduleName,
    busStopIndex = busStopIndex,
    busStopInfos = busStopInfos
)