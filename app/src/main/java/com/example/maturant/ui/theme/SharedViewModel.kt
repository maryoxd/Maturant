import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maturant.ui.theme.ScreenContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {
    private val _styles = mutableStateOf(listOf<String>())
    val styles = _styles

    fun loadStyles(context: Context, resourceId: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                ScreenContents.loadContent(context, resourceId)
            }
            _styles.value = data
        }
    }
}
