package com.busschedule.schedulelist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.busschedule.domain.model.response.schedule.BusSchedule
import com.busschedule.schedulelist.ScheduleListViewModel
import com.busschedule.schedulelist.entity.DayOfWeek
import com.busschedule.schedulelist.entity.DayOfWeekUi
import com.busschedule.util.constant.Constants
import com.example.connex.ui.domain.ApplicationState
import core.designsystem.component.DayOfWeekCard
import core.designsystem.component.HeightSpacer
import core.designsystem.component.WidthSpacer
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@Composable
fun ScheduleListScreen(
    appState: ApplicationState,
    scheduleListViewModel: ScheduleListViewModel = hiltViewModel(),
) {

    val scheduleListUiState by scheduleListViewModel.scheduleListUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        listOf(
            async { scheduleListViewModel.initFCMToken() },
            async { scheduleListViewModel.fetchReadTodaySchedules() }
        ).awaitAll()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF525252))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 10.dp)
    ) {
        HeightSpacer(height = 6.dp)
        ScheduleListAppBar {}
        HeightSpacer(height = 16.dp)
        DayOfWeekSelectArea { scheduleListViewModel.fetchReadDayOfWeekSchedules(it) }
        val lazyListState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            state = lazyListState,
            contentPadding = PaddingValues(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = scheduleListUiState, key = { it.id }) {
                TempScheduleCard(backgroundColor = Color.White, schedule = it) {
                    scheduleListViewModel.fetchDeleteSchedules(it.id)
                }
            }
        }
        Button(
            onClick = { appState.navigate(Constants.REGISTER_BUS_SCHEDULE_ROUTE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "스케줄 등록")
        }
    }
}


@Composable
fun TempScheduleCard(backgroundColor: Color, schedule: BusSchedule, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            Text(text = schedule.name, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Edit, contentDescription = "ic_edit")
            WidthSpacer(width = 4.dp)
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "ic_delete",
                modifier = Modifier.clickable { onDelete() })
        }
        HeightSpacer(height = 4.dp)
        // TODO: 백엔드가 데이터 수정하면 변경
        Text(text = schedule.busStopName)
        HeightSpacer(height = 4.dp)
//        Text(text = "${schedule.busInfos[0].routeno}, ${schedule.busInfos[1].routeno}")
    }
}

@Composable
fun ScheduleListAppBar(onClickSetting: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        /*TODO: 버스링 로고 및 텍스트로 넣을 것 */
        Text(text = "버스링")
        Icon(imageVector = Icons.Outlined.Settings,
            contentDescription = "ic_setting",
            modifier = Modifier
                .size(24.dp)
                .clickable { onClickSetting() })
    }
}


@Composable
fun DayOfWeekSelectArea(requestDaySchedule: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        var dayOfWeeks by remember {
            mutableStateOf(DayOfWeek.entries.map { DayOfWeekUi(dayOfWeek = it) })
        }
        dayOfWeeks.forEach { day ->
            DayOfWeekCard(
                text = day.dayOfWeek.value,
                isSelected = day.isSelected,
                isShadow = true
            ) {
                dayOfWeeks = dayOfWeeks.map { cur ->
                    if (day.dayOfWeek == cur.dayOfWeek) cur.apply {
                        updateSelected(true)
                    } else cur.apply { updateSelected(false) }
                }
                // TODO: 해당 요일 스케줄 api 요청
                requestDaySchedule(day.getDayOfWeeks())
            }
        }
    }
}