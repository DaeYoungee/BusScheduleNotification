package com.busschedule.domain.repository

interface FCMRepository {
    suspend fun postFCMToken(token: String)
}