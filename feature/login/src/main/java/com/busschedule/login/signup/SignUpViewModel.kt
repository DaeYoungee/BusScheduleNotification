package com.busschedule.login.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.busschedule.domain.usecase.login.SignupUseCase
import com.busschedule.login.entity.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel  @Inject constructor(
    private val signupUseCase: SignupUseCase,
) : ViewModel() {
    private val _inputId = MutableStateFlow("")
    val inputId: StateFlow<String> = _inputId.asStateFlow()

    private val _inputPw = MutableStateFlow("")
    val inputPw: StateFlow<String> = _inputPw.asStateFlow()

    private val _inputCheckPw = MutableStateFlow("")
    val inputCheckPw: StateFlow<String> = _inputCheckPw.asStateFlow()

    val signupUiState = combine(inputId, inputPw, inputCheckPw) { inputId, inputPw, inputCheckPw ->
        SignupUiState(inputId = inputId, inputPw = inputPw, inputCheckPw = inputCheckPw)
    }

    fun updateInputId(input: String) {
        _inputId.update { input }
    }

    fun updateInputPw(input: String) {
        _inputPw.update { input }
    }

    fun updateInputCheckPw(input: String) {
        _inputCheckPw.update { input }
    }

    fun fetchSignup(id: String, pw: String, showToast: (String) -> Unit, navigateToLogin: () -> Unit) {
        viewModelScope.launch {
//            when (val result = signupUseCase(LoginUser(name = id, password = pw)).first()) {
//                is com.busschedule.data.network.ApiState.Error -> { showToast(result.errMsg) }
//                com.busschedule.data.network.ApiState.Loading -> TODO()
//                is com.busschedule.data.network.ApiState.Success<*> -> result.onSuccess { showToast(result.msg) }
//                is com.busschedule.data.network.ApiState.NotResponse -> {
//                    Log.d("daeyoung", "exception: ${result.exception}, msg: ${result.message}")
//                    if (result.exception is ConnectException) {
//                    }
//                }
//            }
        }
    }
}