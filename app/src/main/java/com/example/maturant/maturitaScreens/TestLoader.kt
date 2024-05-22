package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maturant.R
import com.example.maturant.maturitaScreens.testStructure.Test
import com.example.maturant.maturitaScreens.testStructure.TestContainer
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.viewModels.MaturitaViewModel
import com.google.gson.Gson

import java.util.Locale

/**
 * LoadTestFromJson
 * LoadTestFromJson slúži na načítanie testu z JSON súboru, pričom používame knižnicu GSON.
 * @param context - Parameter context slúži na získanie súboru s testom.
 * @param fileName - Parameter fileName slúži na získanie názvu súboru s testom.
 * @return - Funkcia vráti test, ktorý sa načíta z JSON súboru.
 */
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

/**
 * DisplayTest
 * DisplayTest slúži na samotné zobrazenie testu so všetkými jeho prvkami. Jedná sa o najdôležitejšiu funkciu,
 * ktorá zobrazuje jednotlivé otázky, odpovede, obrázky a tlačidlá pre odozvu.
 * Úzko spolupracuje s MaturitaViewModel ktorému posiela všetky stavy ktoré sa dejú pri odpovedaní na otázky.
 * @param test - Parameter test slúži na zobrazenie konkrétneho testu.
 * @param viewModel - Parameter viewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami, načítavanie testov, či uchovávanie viacerých stavov, zároveň aj ukladá odpovede a vyhodnocuje ich.
 */
@Composable
fun DisplayTest(test: Test, viewModel: MaturitaViewModel) {
    val totalQuestionsCount = test.sections.sumOf { it.questions.size }
    if (viewModel.userAnswers.size < totalQuestionsCount) {
        viewModel.userAnswers.addAll(List(totalQuestionsCount - viewModel.userAnswers.size) { null })
    }

    val context = LocalContext.current
    val answersState = viewModel.userAnswers
    var totalQuestionIndex = 0
    val showResultsDialog = remember { mutableStateOf(false) }
    var isImageFullScreen by remember { mutableStateOf(false)}

    Column(modifier = Modifier.padding(16.dp)) {
        test.sections.forEach { section ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray,
                border = BorderStroke(2.dp, AppColors.TuftsBlue)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Text(
                        text = "Ukážka ${section.sectionId}: \n${section.text}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    section.imageUrl?.let { imageUrl ->
                        val imageRes = when (imageUrl) {
                            "prijmy_url" -> R.drawable.prijmy_url
                            "zoe_url" -> R.drawable.zoe_url
                            "b_url" -> R.drawable.b_url
                            "vzdelavanie_url" -> R.drawable.vzdelavanie_url
                            "odhad_url" -> R.drawable.odhad_url
                            else -> null
                        }
                        imageRes?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = "Obrázok maturitného testu",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(if (isImageFullScreen) IntrinsicSize.Max else IntrinsicSize.Min)
                                    .clickable {isImageFullScreen = !isImageFullScreen},
                                contentScale = if(isImageFullScreen) ContentScale.FillWidth else ContentScale.Fit
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
                                    viewModel.isTestSubmitted.value && isCorrect && optionLabel == question.correctAnswer -> AppColors.SystemGreen
                                    viewModel.isTestSubmitted.value && isSelected && optionLabel != question.correctAnswer -> AppColors.Red
                                    viewModel.isTestSubmitted.value && !isSelected && optionLabel == question.correctAnswer -> AppColors.CorrectGreen
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
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
        ClickableLegend()
        SubmitTestButtonAndResultsDialog(
            viewModel = viewModel,
            showResultsDialog = showResultsDialog,
            totalQuestionsCount = totalQuestionsCount,
            context = context
        )
    }
}


/**
 * SubmitTestButtonAndResultsDialog
 * SubmitTestButtonAndResultsDialog slúži na zobrazenie tlačidla na "odovzdanie" testu a zobrazenie výsledkov testu.
 * @param viewModel - Parameter viewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami, načítavanie testov, či uchovávanie viacerých stavov, zároveň aj ukladá odpovede a vyhodnocuje ich.
 * @param showResultsDialog - Parameter showResultsDialog slúži na zobrazenie výsledkov testu, uchováva si svoj stav a zobrazuje sa v prípade, že je hodnota true.
 * @param totalQuestionsCount - Parameter totalQuestionsCount slúži na získanie celkového počtu otázok v teste.
 * @param context - Parameter context slúži na získanie kontextu aplikácie.
 */
@Composable
fun SubmitTestButtonAndResultsDialog(
    viewModel: MaturitaViewModel,
    showResultsDialog: MutableState<Boolean>,
    totalQuestionsCount: Int,
    context: Context
) {
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
                        color = if ((testResults?.first ?: 0) >= 36) AppColors.Green else AppColors.Red,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "% ÚSPEŠNOSŤ:",
                        fontWeight = FontWeight.Bold
                    )
                    val successPercentage = ((testResults?.first ?: 0).toDouble() / totalQuestionsCount.toDouble() * 100)
                    val successPercentageString = "%.2f".format(Locale.getDefault(), successPercentage)

                    Text(
                        "$successPercentageString%",
                        color = if (successPercentage >= 70) AppColors.SystemGreen else if (successPercentage >= 50 ) AppColors.Orange else AppColors.Red,
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
                        colors = ButtonDefaults.buttonColors(AppColors.Red)
                    ) {
                        Text("Reset")
                    }
                    Button(
                        onClick = {
                            showResultsDialog.value = false
                            viewModel.saveTestResults(context)
                        },
                        colors = ButtonDefaults.buttonColors(AppColors.Green)
                    ) {
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

/**
 * ClickableLegend
 * ClickableLegend slúži na zobrazenie legendy, ktorá obsahuje informácie o farbách a význame jednotlivých tlačidiel.
 */
@Composable
fun ClickableLegend() {
    var showLegend by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(BorderStroke(2.dp, AppColors.Black))
            .background(AppColors.TuftsBlue)
            .clickable { showLegend = !showLegend }
            .padding(16.dp)
            .fillMaxWidth()

    ) {
        if (showLegend) {
            LegendBox(onClick = { showLegend = false})
        } else {
            Text(
                text = "LEGENDA",
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Black,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * LegendBox
 * LegendBox slúži na zobrazenie legendy, ktorá obsahuje informácie o farbách a význame jednotlivých tlačidiel.
 * @param onClick
 * @receiver
 */
@Composable
fun LegendBox(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = "LEGENDA",
            style = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LegendItem(
            color = Color.Green,
            text = "SPRÁVNA ODPOVEĎ",
            textColor = AppColors.White
        )
        LegendItem(
            color = AppColors.Red,
            text = "NESPRÁVNA ODPOVEĎ",
            textColor = AppColors.White
        )
        LegendItem(
            color = AppColors.CorrectGreen,
            text = "SPRÁVNY VÝBER",
            textColor = AppColors.White
        )
    }
}

/**
 * LegendItem
 * LegendItem slúži ako samostatný "item" (políčko) v legende.
 * @param color - Parameter color slúži na zmenu farby políčka.
 * @param text - Parameter text slúži na zobrazenie textu v políčku.
 * @param textColor - Parameter textColor slúži na zmenu farby textu v políčku.
 */
@Composable
fun LegendItem(color: Color, text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * CustomTextField
 * CustomTextField je composable element ktorý slúži na políčka, kde užívateľ môže zadať svoju odpoveď na maturitné otázky.
 * @param viewModel - Parameter MaturitaViewModel slúži na ukladanie odpovedí, aby neboli vymazané napríklad pri pretočení obrazovky.
 * @param questionIndex - Parameter questionIndex slúži na získanie konkrétnej otázky z testu.
 */
@Composable
fun CustomTextField(viewModel: MaturitaViewModel, questionIndex: Int) {
    var answer by rememberSaveable { mutableStateOf(viewModel.userAnswers.getOrElse(questionIndex) { "" } ?: "") }
    var lastSavedAnswer by rememberSaveable { mutableStateOf(answer) }
    var lastNotEmptyState by rememberSaveable { mutableStateOf(answer.isNotEmpty()) }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.lastResetTimestamp.value) {
        answer = ""
        lastSavedAnswer = ""
        lastNotEmptyState = false
    }

    LaunchedEffect(key1 = viewModel.userAnswers, key2 = questionIndex) {
        answer = viewModel.userAnswers.getOrElse(questionIndex) { "" } ?: ""
        lastSavedAnswer = answer
        lastNotEmptyState = answer.isNotEmpty()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = if (viewModel.isTestSubmitted.value) {
            if (viewModel.questionResults.getOrNull(questionIndex) == true) AppColors.SystemGreen
            else AppColors.Red
        } else if (answer.isNotEmpty()) AppColors.Orange
        else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(4.dp)
    ) {
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
            }
            )
        )
    }
}