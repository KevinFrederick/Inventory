package com.kevinfreyap.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kevinfreyap.domain.manager.INetworkMonitor
import com.kevinfreyap.ui.components.AppOfflineBanner
import com.kevinfreyap.ui.state.LocalIsOffline
import com.kevinfreyap.ui.theme.InventoryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: INetworkMonitor

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.isLoggedIn.collect { isLoggedIn ->
                    if (isLoggedIn) {
                        try {
                            mainViewModel.startListeningForSync()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        setContent {
            InventoryTheme {
                val isLoading by mainViewModel.isLoading.collectAsStateWithLifecycle()
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()

                val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
                val isOffline = !isOnline

                CompositionLocalProvider(LocalIsOffline provides isOffline) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (!isLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .windowInsetsPadding(WindowInsets.statusBars)
                                    .consumeWindowInsets(WindowInsets.statusBars)
                            ) {
                                AppOfflineBanner()

                                Box(modifier = Modifier.weight(1f)) {
                                    RootAppNavigation(
                                        isLoggedIn = isLoggedIn
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}