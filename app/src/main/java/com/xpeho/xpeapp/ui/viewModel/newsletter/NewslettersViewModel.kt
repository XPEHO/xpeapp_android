package com.xpeho.xpeapp.ui.viewModel.newsletter

import android.graphics.BitmapFactory.decodeByteArray
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.Newsletter
import kotlinx.coroutines.launch

class NewsletterViewModel : ViewModel() {

    private val firebaseService = XpeApp.appModule.firebaseService
    private val wordpressRepository = XpeApp.appModule.wordpressRepository

    val state = mutableStateOf(
        listOf<Newsletter>()
    )
    val lastNewsletter = mutableStateOf<Newsletter?>(null)
    val lastNewsletterPreview = mutableStateOf<ImageBitmap?>(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        getNewsletters()
    }

    // Get the newsletters from Firestore and the preview image from WordPress
    private fun getNewsletters() {
        isLoading.value = true
        viewModelScope.launch {
            state.value = firebaseService.fetchNewsletters()
            lastNewsletter.value = state.value.firstOrNull()
            val previewPath = lastNewsletter.value?.picture
            var imageBitmap: ImageBitmap? = null
            if (previewPath != null) {
                val imageBytes = wordpressRepository.getImage(previewPath)
                if (imageBytes != null) {
                    imageBitmap = decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
                } else {
                    Log.e("Newsletters WordPress Image", "getImage a retourné null pour le chemin $previewPath")
                }
            }
            lastNewsletterPreview.value = imageBitmap
            isLoading.value = false
        }
    }

    fun getClassifiedNewsletters(): Map<Int, List<Newsletter>> {
        return state.value.groupBy { it.date.year }
    }

    private fun resetState() {
        state.value = emptyList()
    }

    fun updateState() {
        resetState()
        getNewsletters()
    }
}
