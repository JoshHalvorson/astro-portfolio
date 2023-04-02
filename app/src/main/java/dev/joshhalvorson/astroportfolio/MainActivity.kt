package dev.joshhalvorson.astroportfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.joshhalvorson.astroportfolio.ui.screen.detail.DetailScreen
import dev.joshhalvorson.astroportfolio.ui.screen.gallery.GalleryScreen
import dev.joshhalvorson.astroportfolio.ui.screen.gallery.GalleryViewModel
import dev.joshhalvorson.astroportfolio.ui.theme.AstroportfolioTheme
import dev.joshhalvorson.data.gallery.GalleryRepository
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val viewModel: MainViewModel = hiltViewModel()

            AstroportfolioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        containerColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Astro Portfolio")
                                },
                            )
                        },
                        content = { innerPadding ->
                            NavHost(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                startDestination = "gallery"
                            ) {
                                composable(route = "gallery") {
                                    GalleryScreen(
                                        onImageClicked = {
                                            navController.navigate("detail/$it") {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }

                                composable(
                                    route = "detail/{imageId}",
                                    arguments = listOf(navArgument("imageId") {
                                        type = NavType.StringType
                                    })
                                ) { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("imageId")
                                        ?: kotlin.run { throw IllegalArgumentException("Must pass ID") }
                                    val image = viewModel.getImage(id = id)

                                    DetailScreen(image = image)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}