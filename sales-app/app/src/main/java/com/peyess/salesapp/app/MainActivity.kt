package com.peyess.salesapp.app

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.peyess.salesapp.ui.theme.PeyessTheme
import com.peyess.salesapp.screen.root.SalesAppRoot

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberAnimatedNavController()

            PeyessTheme {
                SalesAppRoot(navHostController = navHostController)
            }
        }

        hideSystemNavigationBar()
    }

    private fun hideSystemNavigationBar() {
        window.insetsController?.apply {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            hide(WindowInsets.Type.navigationBars())
//            hide(WindowInsets.Type.statusBars())
        }
    }
}

