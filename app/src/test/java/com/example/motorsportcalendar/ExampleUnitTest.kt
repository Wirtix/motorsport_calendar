package com.example.motorsportcalendar



import com.example.motorsportcalendar.data.model.F1
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
import com.google.firebase.Timestamp
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationLogicTest {
    private fun formatStartTime(startTime: Any?): String {
        return when (startTime) {
            is Date -> SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(startTime)
            is Timestamp -> SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(startTime.toDate())
            is String -> startTime
            else -> "Time unavailable"
        }
    }

    @Test
    fun isRaceWithin24Hours_futureRace_returnsTrue() {
        val now = System.currentTimeMillis()
        val raceTime = Timestamp(now / 1000 + 60 * 60, 0) // Race in 1 hour
        assertTrue(isRaceWithin24Hours(raceTime))
    }

    @Test
    fun isRaceWithin24Hours_pastRace_returnsFalse() {
        val now = System.currentTimeMillis()
        val raceTime = Timestamp(now / 1000 - 60 * 60 * 25, 0) // Race 25 hours ago
        assertFalse(isRaceWithin24Hours(raceTime))
    }

    @Test
    fun formatStartTime_timestampInput_returnsFormattedString() {
        val timestamp = Timestamp(1678886400, 0) // Example timestamp
        val expectedFormattedTime = "00:00, 15 Mar 2023"
        val actualFormattedTime = formatStartTime(timestamp)
        assertTrue(actualFormattedTime == expectedFormattedTime)
    }

    // Add more test cases for different scenarios

    // Your actual functions (copy them from your production code)

    private fun findClosestRaceInCategory(raceList: List<F1>, category: String): F1? {
        // ... (logic to find the closest upcoming race in the given category)
        return raceList[0]
    }

    private fun isRaceWithin24Hours(raceDate: Timestamp?): Boolean {
        if (raceDate == null) return false

        val now = System.currentTimeMillis() // Get current time in milliseconds
        val twentyFourHoursInMillis = 24 * 60 * 60 * 1000

        val timeDifference =
            raceDate.seconds * 1000 - now // Convert Timestamp seconds to milliseconds
        return timeDifference in 0..twentyFourHoursInMillis
    }
}