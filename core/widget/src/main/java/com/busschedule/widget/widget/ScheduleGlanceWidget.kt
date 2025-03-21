package com.busschedule.widget.widget

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.AnimationState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import com.busschedule.common.constant.Constants
import com.busschedule.model.constant.BusType
import com.busschedule.util.ext.toFormatEnTime
import com.busschedule.widget.R
import com.busschedule.widget.designsystem.style.TextBlackColor
import com.busschedule.widget.designsystem.style.TextBlackColor2
import com.busschedule.widget.designsystem.style.TextColor
import com.busschedule.widget.designsystem.style.rFooter
import com.busschedule.widget.designsystem.style.rTextLabel
import com.busschedule.widget.designsystem.style.sbTitle3
import com.busschedule.widget.widget.receiver.ActionReceiver
import com.busschedule.widget.widget.receiver.WidgetAction

private val destinationKey = ActionParameters.Key<String>(
    Constants.WIDGET_NAVIGATE_ROUTE_OF_MAINACTIVITY
)

class ScheduleGlanceWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*>
        get() = ScheduleStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ScheduleWidget()
        }
    }
}


@Composable
fun ScheduleWidget() {
    when (val state = currentState<ScheduleInfo>()) {
        is ScheduleInfo.Available -> Available(
            state.scheduleId,
            state.scheduleName,
            state.busStop,
            state.busArrivalInfo
        )

        ScheduleInfo.Loading -> Loading()
        ScheduleInfo.Unavailable.JWT401 -> {
            UnauthorizedToken()
        }

        ScheduleInfo.Unavailable.UnExpected -> {
            Unavailable()
        }

        ScheduleInfo.Unavailable.DataIsNull -> {
            NotExistSchedule()
        }
    }
}

@Composable
fun Available(
    scheduleId: String = "",
    scheduleName: String = "",
    busStop: String = "",
    busArrivalInfo: List<BusArrivalData> = emptyList(),
) {
    if (busArrivalInfo.isEmpty()) {
        NotExistArrivalBus(
            scheduleName = scheduleName,
            busStop = busStop,
            busArrivalInfo = busArrivalInfo,
            backgroundColor = BusType.지정.color
        )
    } else {
        ExistArrivalBus(
            scheduleId = scheduleId,
            scheduleName = scheduleName,
            busStop = busStop,
            busArrivalInfo = busArrivalInfo,
            backgroundColor = BusType.find(busArrivalInfo.first().type).color
        )
    }
}

@Composable
fun TitleOfAvailable(
    scheduleName: String = "",
    busStop: String = "",
    contentColor: ColorProvider = TextColor,
) {
    val busImage = R.drawable.image_graybus
    Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(busImage),
            contentDescription = "iamge_bus",
            modifier = GlanceModifier.size(32.dp)
        )
        Spacer(GlanceModifier.width(8.dp))
        Column {
            Text(text = scheduleName, style = sbTitle3.copy(contentColor))
//            Spacer(GlanceModifier.height(4.dp))
            Text(text = busStop, style = rFooter.copy(contentColor))
        }
    }
}

@Composable
fun BusArrivalCard(busArrivalInfo: BusArrivalData) {
    val type = BusType.find(busArrivalInfo.type)
    val busImage = when (type) {
        BusType.간선 -> R.drawable.image_bluebus
        in listOf(BusType.마을, BusType.지선, BusType.일반) -> R.drawable.image_greenbus
        BusType.순환 -> R.drawable.image_yellowbus
        in listOf(BusType.급행, BusType.광역, BusType.직행, BusType.인천) -> R.drawable.image_redbus
        else -> R.drawable.image_graybus
    }
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .cornerRadius(8.dp)
            .background(TextColor)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(busImage),
            contentDescription = "iamge_bus",
            modifier = GlanceModifier.size(16.dp)
        )
        Spacer(GlanceModifier.width(8.dp))
        Text(
            text = busArrivalInfo.bus,
            style = sbTitle3.copy(TextBlackColor2),
            modifier = GlanceModifier.defaultWeight()
        )
        Text(
            text = busArrivalInfo.arrivalTime.toFormatEnTime(),
            style = rTextLabel.copy(TextBlackColor2)
        )

    }
}

@Composable
fun ExistArrivalBus(
    scheduleId: String,
    scheduleName: String = "",
    busStop: String = "",
    busArrivalInfo: List<BusArrivalData> = emptyList(),
    backgroundColor: Color,
) {
    val context = LocalContext.current
    Column(
        modifier = GlanceModifier.fillMaxSize()
            .background(backgroundColor).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleOfAvailable(
            scheduleName = scheduleName,
            busStop = busStop,
            contentColor = TextColor
        )
        Spacer(modifier = GlanceModifier.height(15.dp))

        busArrivalInfo.take(2).forEachIndexed { index, busArrivalData ->
            BusArrivalCard(busArrivalInfo = busArrivalData)
            if (index != 1) {
                Spacer(modifier = GlanceModifier.height(8.dp))
            }
        }

        Spacer(modifier = GlanceModifier.height(15.dp))

        Row(modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Image(
                provider = ImageProvider(R.drawable.ic_previoud_arrow_semibold),
                contentDescription = "ic_previous_arrow",
                modifier = GlanceModifier.defaultWeight().size(24.dp)
                    .clickable(
                        actionSendBroadcast(
                            intent = sendBroadCast(
                                context,
                                WidgetAction.CLICK_PREVIOUS_BUTTON.value,
                                scheduleId
                            )
                        )
                    )
            )
            Image(
                provider = ImageProvider(R.drawable.ic_refresh_semibold),
                contentDescription = "ic_refresh",
                modifier = GlanceModifier.defaultWeight()
                    .clickable(actionRunCallback<UpdateScheduleAction>())
            )
            Image(
                provider = ImageProvider(R.drawable.ic_forward_arrow_semibold),
                contentDescription = "ic_previous_arrow",
                modifier = GlanceModifier.defaultWeight().size(24.dp)
                    .clickable(
                        actionSendBroadcast(
                            intent = sendBroadCast(
                                context = context,
                                actionFlag = WidgetAction.CLICK_NEXT_BUTTON.value,
                                scheduleId = scheduleId
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun NotExistArrivalBus(
    scheduleName: String = "",
    busStop: String = "",
    busArrivalInfo: List<BusArrivalData> = emptyList(),
    backgroundColor: Color,
) {
    val busImage = if (busArrivalInfo.isEmpty()) {
        R.drawable.image_graybus
    } else {
        val type = BusType.find(busArrivalInfo.first().type)
        when (type) {
            BusType.간선 -> R.drawable.image_bluebus
            in listOf(BusType.마을, BusType.지선, BusType.일반) -> R.drawable.image_greenbus
            BusType.순환 -> R.drawable.image_yellowbus
            in listOf(BusType.급행, BusType.광역, BusType.직행, BusType.인천) -> R.drawable.image_redbus
            else -> R.drawable.image_graybus
        }
    }
    Box(
        modifier = GlanceModifier.fillMaxSize()
            .background(backgroundColor).padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(end = 16.dp, top = 10.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                provider = ImageProvider(busImage),
                contentDescription = "iamge_bus",
                modifier = GlanceModifier.size(130.dp)
            )
        }
        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Text(text = scheduleName, style = rFooter)
                Spacer(GlanceModifier.width(8.dp))
                Text(text = busStop, style = rFooter)
            }
            Spacer(GlanceModifier.height(5.dp))
            Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                if (busArrivalInfo.isEmpty()) {
                    Text(text = "도착 예정인\n버스가 없습니다.", style = sbTitle3)
                    return@Row
                }
                busArrivalInfo.take(2).forEach {
                    ArrivalBusContainer(
                        modifier = GlanceModifier.defaultWeight(),
                        busArrivalInfo = it
                    )
                }
                Image(
                    provider = ImageProvider(R.drawable.ic_refresh),
                    contentDescription = "ic_refresh",
                    modifier = GlanceModifier.clickable(actionRunCallback<UpdateScheduleAction>())
                )
            }
        }
    }
}

@Composable
fun ArrivalBusContainer(modifier: GlanceModifier, busArrivalInfo: BusArrivalData) {
    Column(modifier = modifier) {
        Text(text = busArrivalInfo.bus, style = sbTitle3)
        Spacer(GlanceModifier.height(3.dp))
        Text(text = busArrivalInfo.arrivalTime.toFormatEnTime(), style = rFooter)
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(TextColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "로딩", style = sbTitle3.copy(TextBlackColor))
    }
}

@Composable
private fun Unavailable() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(TextColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "실패", style = sbTitle3.copy(TextBlackColor))
    }
}

@Composable
private fun UnauthorizedToken() {
    val busImage = R.drawable.image_graybus
    val backgroundColor = BusType.지정.color
    val context = LocalContext.current
    val intent = Intent().setComponent(
        ComponentName(context.packageName, "com.example.busschedule.MainActivity")
    )

    Box(
        modifier = GlanceModifier.fillMaxSize()
            .background(backgroundColor).padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(end = 16.dp, top = 10.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                provider = ImageProvider(busImage),
                contentDescription = "iamge_bus",
                modifier = GlanceModifier.size(130.dp)
            )
        }
        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = GlanceModifier.clickable(
                    actionStartActivity(
                        intent = intent,
                        parameters = actionParametersOf(destinationKey to "Start"),
                    ),
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "로그인 하러 가기", style = rFooter)
                Spacer(GlanceModifier.width(4.dp))
                Image(
                    provider = ImageProvider(R.drawable.ic_next_arrow),
                    contentDescription = "ic_next_arrow",
                    modifier = GlanceModifier.size(12.dp)
                )
            }

            Spacer(GlanceModifier.height(4.dp))
            Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Text(text = "로그인이\n필요합니다.", style = sbTitle3)
            }
        }
    }
}

@Composable
private fun NotExistSchedule() {
    val busImage = R.drawable.image_graybus
    val backgroundColor = BusType.지정.color
    val context = LocalContext.current
    val intent = Intent().setComponent(
        ComponentName(context.packageName, "com.example.busschedule.MainActivity")
    )
    Box(
        modifier = GlanceModifier.fillMaxSize()
            .background(backgroundColor).padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(end = 16.dp, top = 10.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                provider = ImageProvider(busImage),
                contentDescription = "iamge_bus",
                modifier = GlanceModifier.size(130.dp)
            )
        }
        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = GlanceModifier.clickable(
                    actionStartActivity(
                        intent = intent,
                        parameters = actionParametersOf(destinationKey to "Register"),
                    ),
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "등록 하러 가기", style = rFooter)
                Spacer(GlanceModifier.width(4.dp))
                Image(
                    provider = ImageProvider(R.drawable.ic_next_arrow),
                    contentDescription = "ic_next_arrow",
                    modifier = GlanceModifier.size(12.dp)
                )
            }

            Spacer(GlanceModifier.height(4.dp))
            Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Text(text = "예정된 스케줄이\n없습니다.", style = sbTitle3)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TestWidget(modifier: Modifier = Modifier) {
    Available()
}

fun sendBroadCast(context: Context, actionFlag: String, scheduleId: String): Intent {
    return Intent(context, ActionReceiver::class.java).apply {
        action = actionFlag
        putExtra("scheduleId", scheduleId)
    }
}