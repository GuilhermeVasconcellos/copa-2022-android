package me.dio.copa.catar.notification.scheduler.extensions

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.notification.scheduler.R
import java.time.Duration
import java.time.LocalDateTime

private const val NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE_KEY"
private const val NOTIFICATION_CONTENT_KEY = "NOTIFICATION_CONTENT_KEY"

class NotificationMatchesWorker(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE_KEY) ?: throw IllegalArgumentException("Title is required")
        val content = inputData.getString(NOTIFICATION_CONTENT_KEY) ?: throw IllegalArgumentException("Content is required")

        context.showNotification(title, content)

        return Result.success()
    }

    companion object {
        fun activateNotification(context: Context, match: MatchDomain) {

            // destructure
            val (id, _, _, team1, team2, date) = match
//            val initalDelay = Duration.between(LocalDateTime.now(), date).minusMinutes(5)
            val initalDelay = Duration.ofMinutes(1)
            val inputData = workDataOf(
                NOTIFICATION_TITLE_KEY to "O jogo já vai começar!!!",
                NOTIFICATION_CONTENT_KEY to "${team1.flag} ${team1.displayName} x ${team2.displayName} ${team2.flag}"
            )

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    id,
                    ExistingWorkPolicy.KEEP,
                    createRequest(initalDelay, inputData)
                )

        }

        fun cancelNotification(context: Context, match: MatchDomain) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(match.id)
        }

        private fun createRequest(initalDelay: Duration, inputData: Data): OneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationMatchesWorker>()
            .setInitialDelay(initalDelay)
            .setInputData(inputData)
            .build()


    }
}