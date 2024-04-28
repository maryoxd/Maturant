package com.example.maturant.ui.theme

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    private val _styles = mutableStateOf<List<String>>(emptyList())
    val styles = _styles

    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading
    fun loadStyles(context: Context, resourceId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                Thread.sleep(150)
                context.resources.openRawResource(resourceId).bufferedReader().use { it.readLines() }
            }
            _styles.value = data
            _isLoading.value = false
        }
    }
}
