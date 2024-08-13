package com.example.marvel_app

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.marvel_app.presentation.splash_screen.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = CoroutineScope(Dispatchers.Main)
        var isLoadingComplete = false
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            installSplashScreen().apply {
                setKeepOnScreenCondition{
                    scope.launch {
                        delay(2000L)
                        isLoadingComplete = true
                    }
                    !isLoadingComplete
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // Close splash activity
                finish()
            }
            else{
                SplashScreen(){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    // Close splash activity
                    finish()
                }
            }
        }
    }
}