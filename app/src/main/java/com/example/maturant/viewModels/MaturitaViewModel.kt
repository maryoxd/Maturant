package com.example.maturant.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaturitaViewModel : ViewModel() {
    val showDialog = mutableStateOf(false)
    val selectedYear = mutableStateOf("")

    private val _remainingTime = MutableStateFlow(0)
    private val _answeredQuestions = MutableStateFlow(0)
    private val _totalQuestions = MutableStateFlow(64)

    val remainingTime = _remainingTime.asStateFlow()
    val answeredQuestions = _answeredQuestions.asStateFlow()
    val totalQuestions = _totalQuestions.asStateFlow()

    fun onYearClick(year: String) {
        selectedYear.value = year
        showDialog.value = true
    }

    fun hideDialog() {
        showDialog.value = false
    }

    fun initTimerAndStart(year: String, minutes: Int, seconds: Int) {
        selectedYear.value = year
        val totalSeconds = minutes * 60 + seconds
        startTimer(totalSeconds)
    }

    private fun startTimer(totalSeconds: Int) {
        _remainingTime.value = totalSeconds
        viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun incrementAnsweredQuestions() {
        if (_answeredQuestions.value < _totalQuestions.value) {
            _answeredQuestions.value++
        }
    }

    fun decrementAnsweredQuestions() {
        if (_answeredQuestions.value > 0) {
            _answeredQuestions.value--
        }
    }

    fun resetTimer() {
        _remainingTime.value = 0
    }
}
