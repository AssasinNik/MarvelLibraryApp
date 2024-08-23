package com.example.marvel_app.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.example.marvel_app.util.DailyNotificationWorker
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val workManager: WorkManager
) : AndroidViewModel(application) {

    fun scheduleDailyNotification() {
        DailyNotificationWorker.scheduleWork(getApplication<Application>().applicationContext)
    }
}