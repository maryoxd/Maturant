package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
            section.questions.forEachIndexed { index, question ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        Text(
                            text = "${index + 1}. ${question.questionText}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (question.type == "CHOICE") {
                            var selectedOption by remember { mutableStateOf<Int?>(null) }
                            question.options?.forEachIndexed { idx, option ->
                                val optionLabel = when (idx) {
                                    0 -> "A)"
                                    1 -> "B)"
                                    2 -> "C)"
                                    3 -> "D)"
                                    else -> "${idx+1})"
                                }
                                Button(
                                    onClick = {
                                        if (selectedOption == idx) {
                                            selectedOption = null
                                            viewModel.decrementAnsweredQuestions()
                                        } else {
                                            if (selectedOption == null) {
                                                viewModel.incrementAnsweredQuestions()
                                            }
                                            selectedOption = idx
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        if (selectedOption == idx) AppColors.Orange else AppColors.LightBlue
                                    )
                                ) {
                                    Text("$optionLabel $option", color = AppColors.White)
                                }
                            }
                        } else {
                            var answer by remember { mutableStateOf("") }
                            var answerLogged by remember { mutableStateOf(false) }
                            TextField(
                                value = answer,
                                onValueChange = { newAnswer ->
                                    if (newAnswer.isNotEmpty() && !answerLogged) {
                                        viewModel.incrementAnsweredQuestions()
                                        answerLogged = true
                                    } else if (newAnswer.isEmpty() && answerLogged) {
                                        viewModel.decrementAnsweredQuestions()
                                        answerLogged = false
                                    }
                                    answer = newAnswer
                                },
                                label = { Text("Your Answer") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}





