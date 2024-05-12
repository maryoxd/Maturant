package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
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

fun loadTestFromJson(context: Context, fileName: String): Test? {
    try {
        context.assets.open(fileName).use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer, Charsets.UTF_8)
            val testContainer = Gson().fromJson(json, TestContainer::class.java)
            // Predpokladáme, že každý súbor má len jeden test pre daný rok
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
    val answersState = remember { mutableStateListOf<String?>().apply { addAll(List(totalQuestionsCount) { null }) } }  // Opravené na mutableStateListOf
    var totalQuestionIndex = 0
    val showResultsDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        test.sections.forEach { section ->
            Text(
                text = "Ukážka ${section.sectionId}: \n${section.text}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            section.imageUrl?.let { imageUrl ->
                val imageRes = when(imageUrl) {
                    "prijmy_url" -> R.drawable.prijmy_url
                    else -> null
                }
                imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "Ilustračný obrázok",
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            section.questions.forEach { question ->
                val currentQuestionIndex = totalQuestionIndex++
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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

                                Button(
                                    onClick = {
                                        if (isSelected) {
                                            answersState[currentQuestionIndex] = null
                                            viewModel.decrementAnsweredQuestions()
                                        } else {
                                            if (answersState[currentQuestionIndex] != null) {
                                                viewModel.decrementAnsweredQuestions()
                                            }
                                            answersState[currentQuestionIndex] = optionLabel
                                            viewModel.incrementAnsweredQuestions()
                                            viewModel.saveUserAnswer(currentQuestionIndex, optionLabel)
                                            Log.d("Ukladam", "Ukladam odpoved: $optionLabel na index: $currentQuestionIndex")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (isSelected) AppColors.Orange else AppColors.LightBlue
                                    )
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

        // Pridať tlačidlo na konci testu
        Button(
            onClick = {
                viewModel.evaluateAnswers(test)
                showResultsDialog.value = true
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(AppColors.Red)
        ) {
            Text("ODOVZDAŤ TEST", color = AppColors.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        }
        if (showResultsDialog.value) {
            val testResults = viewModel.testResults.collectAsState().value
            AlertDialog(
                onDismissRequest = { showResultsDialog.value = false },
                title = { Text("Výsledky testu") },
                text = { Text("Správne odpovede: ${testResults?.first ?: 0} z $totalQuestionsCount") },
                confirmButton = {
                    Button(onClick = { showResultsDialog.value = false }) {
                        Text("Zavrieť")
                    }
                }
            )
        }
    }
}

@Composable
fun CustomTextField(viewModel: MaturitaViewModel, questionIndex: Int) {
    var answer by remember { mutableStateOf("") }
    var lastSavedAnswer by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current  // Get the keyboard controller

    TextField(
        value = answer,
        onValueChange = { newAnswer ->
            // Handle the case when text is added or completely removed
            if (newAnswer.isNotEmpty() && answer.isEmpty()) {
                viewModel.incrementAnsweredQuestions()
            } else if (newAnswer.isEmpty() && answer.isNotEmpty()) {
                viewModel.decrementAnsweredQuestions()
            }
            answer = newAnswer
        },
        label = { Text("Vaša odpoveď") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                // Handle the case when focus is lost and there is a change in the answer
                if (!focusState.isFocused && answer != lastSavedAnswer) {
                    if (answer.isEmpty()) {
                        viewModel.saveUserAnswer(questionIndex, "")  // Save as an empty string if cleared
                    } else {
                        viewModel.saveUserAnswer(questionIndex, answer)  // Save new non-empty answer
                    }
                    lastSavedAnswer = answer
                    Log.d("FILL_IN", "Saving answer: $answer at index: $questionIndex")
                }
            },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            // When the Done button is pressed on the keyboard
            if (answer != lastSavedAnswer) {
                if (answer.isEmpty()) {
                    viewModel.saveUserAnswer(questionIndex, "")  // Save as an empty string if cleared
                } else {
                    viewModel.saveUserAnswer(questionIndex, answer)  // Save new non-empty answer
                }
                lastSavedAnswer = answer
                Log.d("FILL_IN", "Saving answer on Done: $answer at index: $questionIndex")
                keyboardController?.hide()  // Hide the keyboard
            }
        })
    )
}
