package com.example.mlexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mlexample.ml.SineModel
import com.example.mlexample.ui.theme.MLExampleTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MLExampleTheme {

                val viewModel: MainViewModel by viewModels {
                    MainViewModel.MainViewModelFactory(applicationContext)
                }
                val state by viewModel.uiState.collectAsState()

                MainScreen(
                    uiState = state,
                    onUserInput = { viewModel.onUserInput(it) }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    onUserInput: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Slider(
            valueRange = 0f..360f,
            value = uiState.inputValue,
            onValueChange = { onUserInput(it) }
        )

        Text("Input Value: ${uiState.inputValue}")
        Text("Output Value: ${uiState.outputValue}")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MLExampleTheme {
        MainScreen(
            uiState = MainUiState(
                inputValue = 0f,
                outputValue = 0f
            ),
            onUserInput = {}
        )
    }
}
