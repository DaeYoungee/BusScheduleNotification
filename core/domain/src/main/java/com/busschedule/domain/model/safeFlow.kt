package com.busschedule.domain.model

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T : Any> safeFlow(apiFunc: suspend () -> Response<ApiResponse<T>>): Flow<ApiState<T>> =
    flow {
        try {
            val res = apiFunc.invoke()
            val apiResult = res.body()!!
            if (res.isSuccessful && apiResult.success) {
                emit(ApiState.Success(data = apiResult.data ?: throw NullPointerException(), msg = apiResult.message))
            } else {
                Log.d("daeyoung", "errorbody: ${res.errorBody()?.string()}")
                val errorBody = apiResult.message
                val errorCode = apiResult.code
                emit(ApiState.Error(errorCode, errorBody))
            }
        } catch (e: Exception) {
            emit(ApiState.NotResponse(message = e.message ?: "", exception = e))
        }
    }.flowOn(Dispatchers.IO)

fun safeFlowAndSaveToken(
    apiFunc: suspend () -> Response<ApiResponse<Token>>,
    saveToken: suspend (String) -> Unit,
): Flow<ApiState<Token>> =
    flow {
        try {
            val res = apiFunc.invoke()
            val apiResult = res.body()!!
            if (res.isSuccessful && apiResult.success) {
                val data = res.body()?.data?.accessToken
                saveToken(data ?: "")
                emit(ApiState.Success(res.body()?.data ?: throw NullPointerException(), msg = apiResult.message))
            } else {
                val errorBody = res.errorBody() ?: throw NullPointerException()
                emit(ApiState.Error( apiResult.code, res.body()?.message ?: throw NullPointerException() ))
            }
        } catch (e: Exception) {
            emit(ApiState.NotResponse(message = e.message ?: "", exception = e))
        }
    }.flowOn(Dispatchers.IO)

fun <T : Any> safeFlowUnit(apiFunc: suspend () -> Response<ApiResponse<T>>): Flow<ApiState<Unit>> =
    flow {
        try {
            val res = apiFunc.invoke()
            val apiResult = res.body()!!
            if (res.isSuccessful && apiResult.success) {
                emit(ApiState.Success(data = Unit, msg = apiResult.message))
            } else {
                val errorBody = apiResult.message
                emit(ApiState.Error(apiResult.code, errorBody))
            }
        } catch (e: Exception) {
            emit(ApiState.NotResponse(message = e.message ?: "", exception = e))
        }
    }.flowOn(Dispatchers.IO)

fun <T : Any> safeFlowNotJson(apiFunc: suspend () -> T): Flow<ApiState<T>> =
    flow {
        try {
            val res = apiFunc()
                emit(ApiState.Success(data = res , msg = ""))
        } catch (e: Exception) {
            emit(ApiState.NotResponse(message = e.message ?: "", exception = e))
        }
    }.flowOn(Dispatchers.IO)