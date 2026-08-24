package com.example

import android.view.WindowManager
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ItProfessionalSectionScreen
import com.example.ui.screens.LoadingScreen
import com.example.ui.screens.ProfileInstallingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SummaryScreen
import com.example.ui.screens.WrongAnswerSolutionScreen
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.TarkShastraTheme
import com.example.ui.viewmodel.QuizUiState
import com.example.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarkShastraTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = NavyBackground
                ) { innerPadding ->
                    TarkAppContent(
                        viewModel = quizViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TarkAppContent(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSecure = uiState is QuizUiState.InGame
    LockScreenOrientationAndSecure(isSecure = isSecure)

    when (val state = uiState) {
        is QuizUiState.HomeScreen -> {
            HomeScreen(viewModel = viewModel, modifier = modifier)
        }
        is QuizUiState.ProfileScreen -> {
            ProfileScreen(viewModel = viewModel, modifier = modifier)
        }
        is QuizUiState.HistoryScreen -> {
            HistoryScreen(viewModel = viewModel, modifier = modifier)
        }
        is QuizUiState.ItProfessionalHubScreen -> {
            ItProfessionalSectionScreen(
                onBack = { viewModel.navigateToHome() }
            )
        }
        is QuizUiState.QuestionLoading -> {
            LoadingScreen(modifier = modifier)
        }
        is QuizUiState.ProfileInstalling -> {
            ProfileInstallingScreen(
                progress = state.progress,
                message = state.message,
                modifier = modifier
            )
        }
        is QuizUiState.InGame -> {
            QuizScreen(state = state, viewModel = viewModel, modifier = modifier)
        }
        is QuizUiState.WrongAnswerSolution -> {
            WrongAnswerSolutionScreen(
                viewModel = viewModel,
                question = state.question,
                selectedOptionIndex = state.selectedOptionIndex,
                onContinue = {
                    viewModel.continueFromWrongAnswerSolution(state)
                }
            )
        }
        is QuizUiState.GameSummary -> {
            SummaryScreen(
                result = state.result,
                lastQuestion = state.lastQuestion,
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }
}

@Composable
fun LockScreenOrientationAndSecure(isSecure: Boolean) {
    val view = LocalView.current
    DisposableEffect(isSecure) {
        val window = (view.context as? android.app.Activity)?.window
        if (isSecure) {
            window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

