package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SarfGameViewModel
import com.example.viewmodel.SarfScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel = remember { SarfGameViewModel() }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val modifierWithPadding = Modifier.padding(innerPadding)
                    
                    when (viewModel.currentScreen) {
                        SarfScreen.Home -> {
                            DashboardScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                        SarfScreen.Campaign -> {
                            CampaignScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                        SarfScreen.PassPlay -> {
                            PassPlayScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                        SarfScreen.Workshop -> {
                            WorkshopScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                        SarfScreen.Companion -> {
                            CompanionScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                        SarfScreen.Library -> {
                            LibraryScreen(
                                viewModel = viewModel,
                                modifier = modifierWithPadding
                            )
                        }
                    }
                }
            }
        }
    }
}
