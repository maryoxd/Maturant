package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.maturant.R
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.viewModels.MaturitaViewModel
import com.google.gson.Gson
import java.util.Locale

fun loadTestFromJson(context: Context, fileName: String): Test? {
    try {
        context.assets.open(fileName).use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer, Charsets.UTF_8)
            val testContainer = Gson().fromJson(json, TestContainer::class.java)
            return testContainer.tests.firstOrNull()
        }
    } catch (e: Exception) {
        Log.e("LoadTest", "Error reading test file: $e")
        return null
    }
}

@Composable
fun DisplayTest(test: Test, viewModel: MaturitaViewModel) {
    val totalQuestionsCount = test.sections.sumOf { it.questions.size }
    if (viewModel.userAnswers.size < totalQuestionsCount) {
        viewModel.userAnswers.addAll(List(totalQuestionsCount - viewModel.userAnswers.size) { null })
    }

    val answersState = viewModel.userAnswers
    var totalQuestionIndex = 0
    val showResultsDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        test.sections.forEach { section ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray,
                border = BorderStroke(2.dp, AppColors.Blue)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Ukážka ${section.sectionId}: \n${section.text}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    section.imageUrl?.let { imageUrl ->
                        val imageRes = when (imageUrl) {
                            "prijmy_url" -> R.drawable.prijmy_url
                            "zoe_url" -> R.drawable.zoe_url
                            else -> null
                        }
                        imageRes?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = "Ilustračný obrázok",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
            section.questions.forEach { question ->
                val currentQuestionIndex = totalQuestionIndex++
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${currentQuestionIndex + 1}. ${question.questionText}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (question.type == "CHOICE") {
                            question.options?.forEachIndexed { idx, option ->
                                val optionLabel = when (idx) {
                                    0 -> "A)"
                                    1 -> "B)"
                                    2 -> "C)"
                                    3 -> "D)"
                                    else -> "${idx + 1})"
                                }
                                val isSelected = answersState[currentQuestionIndex] == optionLabel
                                val isCorrect =
                                    viewModel.questionResults.getOrElse(currentQuestionIndex) { false }
                                        ?: false

                                val buttonColor = when {
                                    viewModel.isTestSubmitted.value && isCorrect && optionLabel == question.correctAnswer -> Color.Green
                                    viewModel.isTestSubmitted.value && isSelected && optionLabel != question.correctAnswer -> AppColors.Red
                                    viewModel.isTestSubmitted.value && !isSelected && optionLabel == question.correctAnswer -> AppColors.Green
                                    isSelected -> AppColors.Orange
                                    else -> AppColors.LightBlue
                                }

                                Button(
                                    onClick = {
                                        if (!viewModel.isTestSubmitted.value) {
                                            if (isSelected) {
                                                answersState[currentQuestionIndex] = null
                                                viewModel.decrementAnsweredQuestions()
                                            } else {
                                                if (answersState[currentQuestionIndex] != null) {
                                                    viewModel.decrementAnsweredQuestions()
                                                }
                                                answersState[currentQuestionIndex] = optionLabel
                                                viewModel.incrementAnsweredQuestions()
                                                viewModel.saveUserAnswer(
                                                    currentQuestionIndex,
                                                    optionLabel
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(buttonColor)
                                ) {
                                    Text("$optionLabel $option", color = AppColors.White)
                                }
                            }
                        } else {
                            CustomTextField(viewModel, currentQuestionIndex)

                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                viewModel.submitTest()
                showResultsDialog.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(AppColors.Red)
        ) {
            Text(
                "ODOVZDAŤ TEST",
                color = AppColors.White,
                fontWeight = FontWeight.Bold
            )
        }
        if (showResultsDialog.value) {
            val testResults = viewModel.testResults.collectAsState().value
            AlertDialog(
                onDismissRequest = { showResultsDialog.value = false },
                title = { Text("Výsledky testu") },
                text = {
                    Column {
                        Text(
                            "SPRÁVNE ODPOVEDE: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${testResults?.first ?: 0} z $totalQuestionsCount",
                            color = if ((testResults?.first
                                    ?: 0) >= 36
                            ) AppColors.Green else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "% ÚSPEŠNOSŤ:",
                            fontWeight = FontWeight.Bold
                        )
                        val successPercentage = ((testResults?.first
                            ?: 0).toDouble() / totalQuestionsCount.toDouble() * 100)
                        val successPercentageString = "%.2f".format(Locale.US, successPercentage)

                        Text(
                            "$successPercentageString%",
                            color = if (successPercentage >= 70) Color.Green else if (successPercentage >= 50) AppColors.Orange else AppColors.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                viewModel.restartTest()
                                showResultsDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("Reset")
                        }
                        Button(
                            onClick = {
                                showResultsDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(AppColors.Green)
                        ) {
                            val context = LocalContext.current
                            viewModel.saveTestResults(context)
                            Text("Uložiť")
                        }
                        Button(
                            onClick = {
                                showResultsDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(AppColors.Black)
                        ) {
                            Text("Zavrieť")
                        }
                    }
                }
            )

        }
    }
}

@Composable
fun CustomTextField(viewModel: MaturitaViewModel, questionIndex: Int) {
    // Použitie rememberSaveable na ukladanie stavu
    var answer by rememberSaveable { mutableStateOf(viewModel.userAnswers.getOrElse(questionIndex) { "" } ?: "") }
    var lastSavedAnswer by rememberSaveable { mutableStateOf(answer) }
    var lastNotEmptyState by rememberSaveable { mutableStateOf(answer.isNotEmpty()) }

    // Klávesnicový ovládač pre skrytie klávesnice
    val keyboardController = LocalSoftwareKeyboardController.current

    // LaunchedEffect na resetovanie odpovede pri resete testu
    LaunchedEffect(viewModel.lastResetTimestamp.value) {
        answer = ""
        lastSavedAnswer = ""
        lastNotEmptyState = false
    }

    // LaunchedEffect na inicializáciu odpovede pri každej rekompozícii
    LaunchedEffect(key1 = viewModel.userAnswers, key2 = questionIndex) {
        answer = viewModel.userAnswers.getOrElse(questionIndex) { "" } ?: ""
        lastSavedAnswer = answer
        lastNotEmptyState = answer.isNotEmpty()
    }

    // Povrch pre TextField
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = if (viewModel.isTestSubmitted.value) {
            if (viewModel.questionResults.getOrNull(questionIndex) == true) Color.Green
            else Color.Red
        } else if (answer.isNotEmpty()) AppColors.Orange
        else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(4.dp)
    ) {
        // TextField s logikou na ukladanie odpovedí a spracovanie klávesnice
        TextField(
            value = answer,
            onValueChange = { newAnswer ->
                if (!viewModel.isTestSubmitted.value) {
                    answer = newAnswer
                    viewModel.saveUserAnswer(questionIndex, newAnswer)
                    if (newAnswer.isNotEmpty() && !lastNotEmptyState) {
                        viewModel.incrementAnsweredQuestions()
                        lastNotEmptyState = true
                    } else if (newAnswer.isEmpty() && lastNotEmptyState) {
                        viewModel.decrementAnsweredQuestions()
                        lastNotEmptyState = false
                    }
                }
            },
            label = { Text("Vaša odpoveď") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = !viewModel.isTestSubmitted.value,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (!viewModel.isTestSubmitted.value && answer.trim() != lastSavedAnswer) {
                    viewModel.saveUserAnswer(questionIndex, answer.trim())
                    lastSavedAnswer = answer.trim()
                    keyboardController?.hide()
                }
            })
        )
    }
}