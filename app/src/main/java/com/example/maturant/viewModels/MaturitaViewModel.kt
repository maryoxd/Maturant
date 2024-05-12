package com.example.maturant.viewModels


import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maturant.maturitaScreens.loadTestFromJson
import com.example.maturant.maturitaScreens.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaturitaViewModel : ViewModel() {
    val showDialog = mutableStateOf(false)
    val selectedYear = mutableStateOf("")

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _remainingTime = MutableStateFlow(0)
    private val _answeredQuestions = MutableStateFlow(0)
    private val _totalQuestions = MutableStateFlow(64)

    val remainingTime = _remainingTime.asStateFlow()
    val answeredQuestions = _answeredQuestions.asStateFlow()
    val totalQuestions = _totalQuestions.asStateFlow()

    private val _currentTest = mutableStateOf<Test?>(null)
    val currentTest: State<Test?> = _currentTest

    private val _userAnswers = mutableStateListOf<String?>()
    private val _testResults = MutableStateFlow<Pair<Int, Int>?>(null)
    val testResults: StateFlow<Pair<Int, Int>?> = _testResults.asStateFlow()

    fun loadTest(context: Context, fileName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(500)
            val loadedTest = loadTestFromJson(context, fileName)
            if (loadedTest != null) {
                _currentTest.value = loadedTest
                _totalQuestions.value = loadedTest.sections.sumOf { it.questions.size }
            } else {
                Log.d("TestLoading", "No test found or error loading the test")
            }
            _isLoading.value = false
        }
    }

    fun saveUserAnswer(questionIndex: Int, answer: String) {
        while (questionIndex >= _userAnswers.size) {
            _userAnswers.add(null)  // Ensure the list is large enough
        }
        _userAnswers[questionIndex] = answer
        Log.d("UserAnswers", _userAnswers.size.toString())
    }

    fun evaluateAnswers(test: Test) {
        var correctCount = 0
        var questionIndex = 0
        test.sections.forEach { section ->
            section.questions.forEach { question ->
                val userAnswer = _userAnswers.getOrNull(questionIndex)?.trim()?.lowercase()
                val correctAnswers = when (question.type) {
                    "CHOICE" -> listOf(question.correctAnswer.lowercase())
                    "FILL_IN" -> question.correctAnswer.split("|").map { it.trim().lowercase() }
                    else -> emptyList()
                }
                if (userAnswer in correctAnswers) {
                    correctCount++
                }
                questionIndex++
            }
        }
        _testResults.value = Pair(correctCount, _userAnswers.size)
    }

    fun resetResults() {
        _testResults.value = null
        _userAnswers.clear()
    }



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
        _answeredQuestions.value = 0
    }
}