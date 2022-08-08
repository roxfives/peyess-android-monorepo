package com.peyess.salesapp.app

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.AppAuthenticationState
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.screen.root.SalesAppRoot
import com.peyess.salesapp.ui.theme.SalesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SalesApp)
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberAnimatedNavController()

            val viewModel: MainViewModel = mavericksViewModel()
            val authState by viewModel.collectAsState(MainAppState::authState)

            LaunchedEffect(authState) {
                when (authState) {
                    is AppAuthenticationState.Unauthenticated -> {
                        navHostController.backQueue.clear()
                        navHostController.navigate(SalesAppScreens.StoreAuthentication.name)
                    }

                    else -> {
                        navHostController.backQueue.clear()
                        navHostController.navigate(SalesAppScreens.UserAuthentication.name)
                    }
                }
            }

            SalesAppTheme {
                SalesAppRoot(navHostController = navHostController)
            }
        }

        hideSystemNavigationBar()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // TODO: Control timeout for auto-blocking due to inactivity here

        return super.onTouchEvent(event)
    }

    @Suppress("DEPRECATION")
    private fun hideSystemNavigationBar() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            window.insetsController?.apply {
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                hide(WindowInsets.Type.navigationBars())
            }
        }
    }
}

