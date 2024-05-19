package com.example.maturant.viewModels


import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maturant.maturitaScreens.loadTestFromJson
import com.example.maturant.maturitaScreens.Test
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File


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
    val userAnswers: MutableList<String?> = _userAnswers
    private val _testResults = MutableStateFlow<Pair<Int, Int>?>(null)
    val testResults: StateFlow<Pair<Int, Int>?> = _testResults.asStateFlow()

    private var _isTestSubmitted = mutableStateOf(false)
    var isTestSubmitted: MutableState<Boolean> = _isTestSubmitted

    private val _questionResults = mutableStateListOf<Boolean?>()
    val questionResults: List<Boolean?> = _questionResults

    private var _testDurationMinutes = mutableIntStateOf(0)
    private var _testDurationSeconds = mutableIntStateOf(0)

    private var timerJob: Job? = null

    private val _lastResetTimeStamp = mutableLongStateOf(System.currentTimeMillis())
    val lastResetTimestamp: State<Long> = _lastResetTimeStamp

    private val _wasSaved = mutableStateOf<Boolean>(false)
    val wasSaved: State<Boolean> = _wasSaved

    fun submitTest() {
        _isTestSubmitted.value = true
        evaluateAnswers(currentTest.value!!)
    }

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
            _userAnswers.add(null)
        }
        _userAnswers[questionIndex] = answer
        Log.d("UserAnswers", _userAnswers.size.toString())
    }




    fun restartTest() {
        _isTestSubmitted.value = false
        _userAnswers.clear()
        _questionResults.clear()
        _testResults.value = null
        _answeredQuestions.value = 0
        resetTimer()
        _wasSaved.value = false
        _lastResetTimeStamp.longValue = System.currentTimeMillis()
        initTimerAndStart(selectedYear.value, _testDurationMinutes.intValue, _testDurationSeconds.intValue)
    }

    private fun evaluateAnswers(test: Test) {
        var correctCount = 0
        var questionIndex = 0
        _questionResults.clear()

        test.sections.forEach { section ->
            section.questions.forEach { question ->
                val userAnswer = _userAnswers.getOrNull(questionIndex)?.trim()?.lowercase()
                val correctAnswers = question.correctAnswer.lowercase().split("|").map { it.trim() }
                val isCorrect = correctAnswers.contains(userAnswer)
                _questionResults.add(isCorrect)
                if (isCorrect) correctCount++
                questionIndex++
            }
        }
        _testResults.value = Pair(correctCount, _userAnswers.size)
    }



    fun resetResults() {
        _testResults.value = null
        _answeredQuestions.value = 0
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
        _testDurationMinutes.intValue = minutes
        _testDurationSeconds.intValue = seconds
        selectedYear.value = year
        val totalSeconds = minutes * 60 + seconds
        startTimer(totalSeconds)
    }

    private fun startTimer(totalSeconds: Int) {
        _remainingTime.value = totalSeconds
        timerJob?.cancel()  // Zruší predchádzajúcu coroutine, ak existuje
        timerJob = viewModelScope.launch {
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
        viewModelScope.launch {
            timerJob?.cancelAndJoin()
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun resumeTimer() {
        startTimer(_remainingTime.value)
    }

    fun saveTestResults(context: Context) {
        _wasSaved.value = true
        val resultsFile = File(context.filesDir, "test_results.txt")
        val testYear = selectedYear.value
        val correctAnswers = testResults.value?.first ?: 0
        val totalQuestions = testResults.value?.second ?: 0
        val successPercentage = (correctAnswers.toDouble() / totalQuestions * 100).toString()
        Log.d("ResultsScreen", "halo")
        // Načítanie existujúcich výsledkov
        val existingResults = if (resultsFile.exists()) {
            resultsFile.readLines().toMutableList()
        } else {
            mutableListOf()
        }

        // Nájdeme a prepíšeme existujúci záznam pre daný rok
        val newResult = "$testYear,$correctAnswers,$totalQuestions,$successPercentage"
        var resultUpdated = false

        for (i in existingResults.indices) {
            if (existingResults[i].startsWith("$testYear,")) {
                existingResults[i] = newResult
                resultUpdated = true
                break
            }
        }

        if (!resultUpdated) {
            existingResults.add(newResult)
        }

        // Uloženie aktualizovaných výsledkov do súboru
        resultsFile.writeText(existingResults.joinToString("\n"))
    }

}