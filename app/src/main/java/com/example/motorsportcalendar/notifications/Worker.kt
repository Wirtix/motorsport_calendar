package com.example.motorsportcalendar.notifications

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.motorsportcalendar.data.model.F1
import com.example.motorsportcalendar.screens.SettingsViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RaceNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val shownRaceNotifications = sharedPreferences.getStringSet("shown_race_notifications", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        Log.d("NOTTEST", "Worker started")
        // 1. Get notification preferences using SettingsViewModel
        val settingsViewModel = SettingsViewModel(applicationContext as Application)
        val categoriesToNotify = mutableSetOf<String>()
        if (settingsViewModel.notifyF1.value) categoriesToNotify.add("F1")
        if (settingsViewModel.notifyWEC.value) categoriesToNotify.add("WEC")
        if (settingsViewModel.notifyWRC.value) categoriesToNotify.add("WRC")
        if (settingsViewModel.notifyMotoGP.value) categoriesToNotify.add("MotoGP")
        if (settingsViewModel.notifyNASCAR.value) categoriesToNotify.add("NASCAR")

        // 2. Get sorted race list (you might need to fetch this from a data source)
        val sortedRaceList = getSortedRaceList()



        // 3. Find closest races and send notifications
        // ... (rest of your notification logic)
        for (category in categoriesToNotify) {
            val closestRace = findClosestRaceInCategory(sortedRaceList, category)
            closestRace.let {
                val startTimeFormatted = formatStartTime(it.date)
                val qualyTimeFormatted = formatStartTime(it.quali_date)
                val notificationHandler = NotificationHandler(applicationContext)
                Log.d("NOTTEST", "Found closest race in $category: ${it.name}")
                if (isRaceWithin48Hours(it.date) && !shownRaceNotifications.contains("${it.id}_48")) {
                    notificationHandler.showSimpleNotification(it, "This is race week of ${it.name}")

                    shownRaceNotifications.add("${it.id}_48")
                }
                if (isRaceWithin24Hours(it.date) && !shownRaceNotifications.contains("${it.id}_24")) {
                    notificationHandler.showSimpleNotification(it, "Race starts tomorrow: $startTimeFormatted")
                    shownRaceNotifications.add("${it.id}_24")
                }
                if (isRaceWithin1Hour(it.date) && !shownRaceNotifications.contains("${it.id}_1")) {
                    notificationHandler.showSimpleNotification(it, "Race starts in 1 hour: $startTimeFormatted")
                    shownRaceNotifications.add("${it.id}_1")
                }
                if (isQualiWithin1Hour(it.quali_date) && !shownRaceNotifications.contains("${it.id}_q1")) {
                    notificationHandler.showSimpleNotification(it, "Qualification starts in 1 hour: $qualyTimeFormatted")
                    shownRaceNotifications.add("${it.id}_q1")
                }
                sharedPreferences.edit().putStringSet("shown_race_notifications", shownRaceNotifications).apply()
            }
        }

        return Result.success()
    }

    // ... (helper functions for getting race list, etc.)
}
suspend fun getSortedRaceList(): List<F1> {
    val db = FirebaseFirestore.getInstance()
    val raceCollection =db.collection("F1") // Replace "races" with your collection name

    try {
        val querySnapshot = raceCollection.orderBy("date").get().await() // Sort by date
        val raceList = querySnapshot.toObjects(F1::class.java)
        return raceList
    } catch (e: Exception) {
        // Handle potential errors (e.g., network issues, database exceptions)
        return emptyList()
    }
}

private fun findClosestRaceInCategory(raceList: List<F1>, category:String): F1 {
    // ... (logic to find the closest upcoming race in the given category)
    val calendarToday = Calendar.getInstance()
    for (race in raceList){
        if(race.category == category && race.date?.toDate()?.after(calendarToday.time) == true){
            return race
        }
    }
    if (raceList.isEmpty()) {
        return F1()
    }
    return raceList[0]
}

private fun isRaceWithin24Hours(raceDate: Timestamp?): Boolean {
    if (raceDate == null) return false

    val now = System.currentTimeMillis() // Get current time in milliseconds
    val twentyFourHoursInMillis = 24 * 60 * 60 * 1000
    Log.d("NOTTEST24", "${raceDate.seconds*1000},  $now")
    val timeDifference = raceDate.seconds * 1000 - now  // Convert Timestamp seconds to milliseconds
    Log.d("NOTTEST24", "Time difference: $timeDifference")
    return timeDifference in 0..twentyFourHoursInMillis
}
private fun isRaceWithin48Hours(raceDate: Timestamp?): Boolean {
    if (raceDate == null) return false

    val now = System.currentTimeMillis() // Get current time in milliseconds
    val twentyFourHoursInMillis = 2*24 * 60 * 60 * 1000
    Log.d("NOTTEST48", "${raceDate.seconds*1000},  $now")
    val timeDifference = raceDate.seconds * 1000 - now  // Convert Timestamp seconds to milliseconds
    Log.d("NOTTEST48", "Time difference: $timeDifference")
    return timeDifference in 0..twentyFourHoursInMillis
}
private fun isRaceWithin1Hour(raceDate: Timestamp?): Boolean {
    if (raceDate == null) return false

    val now = System.currentTimeMillis() // Get current time in milliseconds
    val twentyFourHoursInMillis = 60 * 60 * 1000
    Log.d("NOTTEST1", "${raceDate.seconds*1000},  $now")
    val timeDifference = raceDate.seconds * 1000 - now  // Convert Timestamp seconds to milliseconds
    Log.d("NOTTEST1", "Time difference: $timeDifference")
    return timeDifference in 0..twentyFourHoursInMillis
}
private fun isQualiWithin1Hour(qualiDate: Timestamp?): Boolean {
    if (qualiDate == null) return false

    val now = System.currentTimeMillis() // Get current time in milliseconds
    val twentyFourHoursInMillis = 60 * 60 * 1000
    Log.d("NOTTESTq1", "${qualiDate.seconds*1000},  $now")
    val timeDifference = qualiDate.seconds * 1000 - now  // Convert Timestamp seconds to milliseconds
    Log.d("NOTTESTq1", "Time difference: $timeDifference")
    return timeDifference in 0..twentyFourHoursInMillis
}
private fun formatStartTime(startTime: Any?): String {
    return when (startTime) {
        // ...(other cases)
        is Timestamp -> SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(startTime.toDate())
        // ... (other cases)
        else -> "Time unavailable"
    }
}
