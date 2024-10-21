package com.example.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.githubexplorer.navigation.NavigationStack
import com.example.githubexplorer.ui.theme.GithubExplorerTheme

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No Snackbar Host State")
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubExplorerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                var canNavigateBack by remember { mutableStateOf(false) }
                navController.addOnDestinationChangedListener { controller, _, _ ->
                    canNavigateBack = controller.previousBackStackEntry != null
                }
                CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text("Github Repo")
                                },
                                navigationIcon = {
                                    if (canNavigateBack) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            NavigationStack(navController)
                        }
                    }
                }
            }
        }
    }
}