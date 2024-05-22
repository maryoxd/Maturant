package com.example.maturant.viewModels


import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maturant.maturitaScreens.loadTestFromJson
import com.example.maturant.maturitaScreens.testStructure.Test
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale


/**
 * MaturitaViewModel
 * MaturitaViewModel slúži na ukladanie a zdieľanie dát medzi jednotlivými obrazovkami, ako je napríklad zobrazenie testu, zobrazenie výsledkov testu, zobrazenie času, ktorý zostal na testovanie a podobne.
 * @constructor - Vytvára dáta pre MaturitaViewModel
 */
class MaturitaViewModel : ViewModel() {
    val showDialog = mutableStateOf(false)
    val selectedYear = mutableStateOf("")

    val isTimeUpDialogShown = mutableStateOf(false)

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

    private val _wasSaved = mutableStateOf(false)
    var wasSaved: MutableState<Boolean> = _wasSaved

    /**
     * SubmitTest
     * SubmitTest slúži na vyhodnotenie testu.
     */
    fun submitTest() {
        _isTestSubmitted.value = true
        evaluateAnswers(currentTest.value!!)
    }

    /**
     * LoadTest
     * LoadTest slúži na načítanie testu z JSON súboru.
     * @param context - Parameter context slúži na získanie prístupu k súboru s testom.
     * @param fileName - Parameter fileName slúži na získanie názvu súboru s testom.
     */
    fun loadTest(context: Context, fileName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(500)
            val loadedTest = loadTestFromJson(context, fileName)
            if (loadedTest != null) {
                _currentTest.value = loadedTest
                _totalQuestions.value = loadedTest.sections.sumOf { it.questions.size }
            }
            _isLoading.value = false
        }
    }

    /**
     * SaveUserAnswer
     * SaveUserAnswer slúži na uloženie odpovede používateľa.
     * @param questionIndex
     * @param answer
     */
    fun saveUserAnswer(questionIndex: Int, answer: String) {
        while (questionIndex >= _userAnswers.size) {
            _userAnswers.add(null)
        }
        _userAnswers[questionIndex] = answer
    }

    /**
     * RestartTest
     * RestartTest slúži na reštartovanie testu.
     */
    fun restartTest() {
        isTimeUpDialogShown.value = false
        _isTestSubmitted.value = false
        _userAnswers.clear()
        _questionResults.clear()
        _testResults.value = null
        _answeredQuestions.value = 0
        resetTimer()
        _wasSaved.value = false
        _lastResetTimeStamp.longValue = System.currentTimeMillis()
        initTimerAndStart(
            selectedYear.value,
            _testDurationMinutes.intValue,
            _testDurationSeconds.intValue
        )
    }

    /**
     * EvaluateAnswers
     * EvaluateAnswers slúži na vyhodnotenie odpovedí užívateľa.
     * @param test - Parameter test slúži na získanie daného testu.
     */
    fun evaluateAnswers(test: Test) {
        var correctCount = 0
        var questionIndex = 0
        _questionResults.clear()

        test.sections.forEach { section ->
            section.questions.forEach { question ->
                val userAnswer = _userAnswers.getOrNull(questionIndex)?.trim()?.lowercase()
                val correctAnswers = question.correctAnswer.lowercase().split("|").map { it.trim() }

                val isCorrect = if (correctAnswers.contains("*")) {
                    true
                } else {
                    correctAnswers.contains(userAnswer)
                }

                _questionResults.add(isCorrect)
                if (isCorrect) correctCount++
                questionIndex++
            }
        }
        _testResults.value = Pair(correctCount, _userAnswers.size)
    }


    /**
     * ResetResults
     * ResetResults slúži na resetovanie výsledkov testu a odpovedí používateľa.
     */
    fun resetResults() {
        _testResults.value = null
        _answeredQuestions.value = 0
        _userAnswers.clear()
    }

    /**
     * OnYearClick
     * OnYearClick slúži na zobrazenie dialógu s označeným rokom testu.
     * @param year - Parameter year slúži na označenie roka testu.
     */
    fun onYearClick(year: String) {
        selectedYear.value = year
        showDialog.value = true
    }

    /**
     * HideDialog
     * HideDialog slúži na skrytie dialógu.
     */
    fun hideDialog() {
        showDialog.value = false
    }

    /**
     * InitTimerAndStart
     * InitTimerAndStart slúži na inicializáciu času a spustenie časovača.
     * @param year - Parameter year slúži na označenie roka testu.
     * @param minutes - Parameter minutes slúži na nastavenie minút.
     * @param seconds - Parameter seconds slúži na nastavenie sekúnd.
     */
    fun initTimerAndStart(year: String, minutes: Int, seconds: Int) {
        _testDurationMinutes.intValue = minutes
        _testDurationSeconds.intValue = seconds
        selectedYear.value = year
        val totalSeconds = minutes * 60 + seconds
        startTimer(totalSeconds)
    }

    /**
     * StartTimer
     * StartTimer slúži na spustenie časovača.
     * @param totalSeconds - Parameter totalSeconds slúži na nastavenie celkového počtu sekúnd.
     */
    fun startTimer(totalSeconds: Int) {
        _remainingTime.value = totalSeconds
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    /**
     * IncrementAnsweredQuestions
     * IncrementAnsweredQuestions slúži na pripočítanie zodpovedaných otázok.
     */
    fun incrementAnsweredQuestions() {
        if (_answeredQuestions.value < _totalQuestions.value) {
            _answeredQuestions.value++
        }
    }

    /**
     * DecrementAnsweredQuestions
     * DecrementAnsweredQuestions slúži na odpočítanie zodpovedaných otázok.
     */
    fun decrementAnsweredQuestions() {
        if (_answeredQuestions.value > 0) {
            _answeredQuestions.value--
        }
    }

    /**
     * ResetTimer
     * ResetTimer slúži na resetovanie časovača.
     */
    fun resetTimer() {
        viewModelScope.launch {
            timerJob?.cancelAndJoin()
        }
    }

    /**
     * PauseTimer
     * PauseTimer slúži na pozastavenie časovača.
     */
    fun pauseTimer() {
        timerJob?.cancel()
    }

    /**
     * ResumeTimer
     * ResumeTimer slúži na obnovenie časovača.
     */
    fun resumeTimer() {
        startTimer(_remainingTime.value)
    }

    /**
     * SaveTestResults
     * SaveTestResults slúži na uloženie výsledkov testu.
     * @param context
     */
    fun saveTestResults(context: Context) {
        _wasSaved.value = true
        val resultsFile = File(context.filesDir, "test_results.txt")
        val testYear = selectedYear.value
        val correctAnswers = testResults.value?.first ?: 0
        val totalQuestions = testResults.value?.second ?: 0
        val successPercentage = (correctAnswers.toDouble() / totalQuestions * 100).toString()
        val currentDate =
            DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(Date())
        val existingResults = if (resultsFile.exists()) {
            resultsFile.readLines().toMutableList()
        } else {
            mutableListOf()
        }

        val newResult = "$testYear,$correctAnswers,$totalQuestions,$successPercentage,$currentDate"
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

        resultsFile.writeText(existingResults.joinToString("\n"))
    }
}