package com.example.weathersnap.ui.camera

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.util.CompressionResult
import com.example.weathersnap.util.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _compressionResult = MutableSharedFlow<CompressionResult>()
    val compressionResult = _compressionResult.asSharedFlow()

    fun onImageCaptured(context: Context, imagePath: String) {
        viewModelScope.launch {
            val result = ImageCompressor.compressImage(context, imagePath)
            _compressionResult.emit(result)
        }
    }
}
