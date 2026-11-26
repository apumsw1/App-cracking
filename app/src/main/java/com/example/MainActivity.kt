package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.NavGraph
import com.example.ui.theme.AdminLoaderTheme
import com.example.viewmodel.AdminViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminLoaderTheme {
                val viewModel: AdminViewModel = viewModel()
                NavGraph(viewModel = viewModel)
            }
        }
    }
}
