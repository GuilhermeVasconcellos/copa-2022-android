package me.dio.copa.catar.features

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatchesWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme
import me.dio.copa.catar.viewmodel.MainUiAction
import me.dio.copa.catar.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeActions()
        setContent {
            Copa2022Theme {
                val state = viewModel.state.collectAsState()
                Log.i("Copa2022", "onCreate: ${state.value.matches}")
                MainScreen(matches = state.value.matches, viewModel::toggleNotification)
            }
        }
    }

    private fun observeActions() {
        viewModel.action.observe(this) {
            when(it) {
                is MainUiAction.DisableNotification ->
                    NotificationMatchesWorker.cancelNotification(applicationContext, it.match)
                is MainUiAction.EnableNotification ->
                    NotificationMatchesWorker.activateNotification(applicationContext, it.match)
                is MainUiAction.MatchesNotFound -> TODO()
                MainUiAction.Unexpected -> TODO()
            }
        }
    }

}

@Composable
fun MainScreen() {

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    Copa2022Theme {
        Greeting("Android")
    }
}
