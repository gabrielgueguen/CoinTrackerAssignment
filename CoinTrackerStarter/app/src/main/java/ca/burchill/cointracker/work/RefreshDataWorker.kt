package ca.burchill.cointracker.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ca.burchill.cointracker.database.getDatabase
import ca.burchill.cointracker.repository.CoinsRepository
import retrofit2.HttpException
import timber.log.Timber

/*
 * Author: Gabriel Gueguen
 *  Title: Android Dev - Coin Tracker Application - Class to handle work of refreshing data
 *   Date: 2021-04-08
 *   NOTE: Based on Android Codelab 9.2
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params){
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = CoinsRepository(database)
        try{
            repository.refreshCoins()
            Timber.d("Work request for sync is run")
        } catch(e: HttpException){
            return Result.retry()
        }
        return Result.success();
    }

    companion object {
        const val WORK_NAME = "ca.burchill.cointracker.work.RefreshDataWorker"
    }
}