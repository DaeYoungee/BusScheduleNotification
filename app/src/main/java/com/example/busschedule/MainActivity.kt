package com.example.busschedule

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.busschedule.common.constant.Constants
import com.busschedule.navigate.RootNavHost
import core.designsystem.theme.BusScheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Widget으로 받은 route 값
        val route = intent.extras?.getString(Constants.WIDGET_NAVIGATE_ROUTE_OF_MAINACTIVITY)
            ?: "Start"

        Log.d("daeyoung", "mainActivity, route: $route")

        enableEdgeToEdge()
        setContent {
            BusScheduleTheme {
                RootNavHost(route = route)
            }
        }
    }
}