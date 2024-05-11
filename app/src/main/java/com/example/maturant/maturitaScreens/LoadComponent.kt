package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    val answersState = remember { mutableStateListOf<Int?>().apply { addAll(List(totalQuestionsCount) { null }) } }
    var totalQuestionIndex = 0

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
                                val isSelected = answersState[currentQuestionIndex] == idx

                                Button(
                                    onClick = {
                                        if (isSelected) {
                                            answersState[currentQuestionIndex] = null
                                            viewModel.decrementAnsweredQuestions()
                                        } else {
                                            if (answersState[currentQuestionIndex] != null) {
                                                viewModel.decrementAnsweredQuestions()
                                            }
                                            answersState[currentQuestionIndex] = idx
                                            viewModel.incrementAnsweredQuestions()
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
                            var answer by remember { mutableStateOf("") }
                            TextField(
                                value = answer,
                                onValueChange = { newAnswer ->
                                    if (newAnswer.isNotEmpty() && answer.isEmpty()) {
                                        viewModel.incrementAnsweredQuestions()
                                    } else if (newAnswer.isEmpty() && answer.isNotEmpty()) {
                                        viewModel.decrementAnsweredQuestions()
                                    }
                                    answer = newAnswer
                                },
                                label = { Text("Vaša odpoveď") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}



