package com.example.mlexample

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mlexample.ml.SineModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


@SuppressLint("StaticFieldLeak")
class MainViewModel(
    context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())

    val uiState = _uiState
    private val model = SineModel.newInstance(context)

    fun onUserInput(input: Float) {
       uiState.update {
           it.copy(
               inputValue = input
           )
       }

        val inputRadians = input * Math.PI / 180.0
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), org.tensorflow.lite.DataType.FLOAT32)
        inputFeature0.loadArray(floatArrayOf(inputRadians.toFloat()))

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        uiState.update {
            it.copy(
                outputValue = outputFeature0.floatArray[0]
            )
        }
    }

    class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}