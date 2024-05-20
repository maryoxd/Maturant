package com.example.maturant.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maturant.topics.dataInfoStructure.AuthorInfo
import com.example.maturant.topics.dataInfoStructure.StyleInfo
import com.example.maturant.topics.dataInfoStructure.loadAuthorsFromJson
import com.example.maturant.topics.dataInfoStructure.loadStylesFromJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    private val _styles = mutableStateOf<Map<String, StyleInfo>>(emptyMap())
    val styles: State<Map<String, StyleInfo>> = _styles

    private val _authors = mutableStateOf<Map<String, AuthorInfo>>(emptyMap())
    val authors: State<Map<String, AuthorInfo>> = _authors

    private val _isLoadingStyles = mutableStateOf(false)
    val isLoadingStyles: State<Boolean> = _isLoadingStyles

    private val _isLoadingAuthors = mutableStateOf(false)
    val isLoadingAuthors: State<Boolean> = _isLoadingAuthors

    private val _isNavigationLocked = mutableStateOf(false)
    val isNavigationLocked: State<Boolean> = _isNavigationLocked

    fun loadStyles(context: Context) {
        if (_styles.value.isNotEmpty()) return
        _isLoadingStyles.value = true
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                loadStylesFromJson(context)
            }
            delay(250)
            _styles.value = data.associateBy { it.name }
            _isLoadingStyles.value = false
        }
    }

    fun loadAuthors(context: Context) {
        if (_authors.value.isNotEmpty()) return

        _isLoadingAuthors.value = true
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                loadAuthorsFromJson(context)
            }
            delay(250)
            _authors.value = data.associateBy { it.name }
            _isLoadingAuthors.value = false
        }
    }

    fun lockNavigation() {
        _isNavigationLocked.value = true
        viewModelScope.launch {
            delay(1000)
            _isNavigationLocked.value = false
        }
    }
}

