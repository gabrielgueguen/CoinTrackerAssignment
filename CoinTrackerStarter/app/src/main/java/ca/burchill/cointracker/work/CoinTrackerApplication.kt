package ca.burchill.cointracker.work

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/*
 * Editor: Gabriel Gueguen
 *  Title: Android Dev - Coin Tracker Application - Class to handle work of refreshing data
 *   Date: 2021-04-08
 * Origin: GitHub Starter Files for Project
 *   Note: Updates based on Android Codelab 9.2
 */

class CoinTrackerApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    /**
     * Setup WorkManager background job to 'fetch' new network data every 15 minutes.
     */
    @Suppress("DEPRECATION")
    private fun setupRecurringWork(){
        // Edit these constraints as needed
        // Changed default behaviour to not run on low battery as this app is not designed to be critical.
        // Implementing a toggle option on the UI for low battery options would a great alternative to setting this here.
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        Timber.d("Periodic Work request for sync is scheduled")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree()) //used for debugging, will log messages to the Logcat Console
            setupRecurringWork()
        }
    }
}