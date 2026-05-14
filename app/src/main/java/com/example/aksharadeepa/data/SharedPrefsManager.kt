package com.example.aksharadeepa.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class SharedPrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("akshara_deepa_prefs", Context.MODE_PRIVATE)

    fun setChapterCompleted(subjectName: String, chapterName: String, isCompleted: Boolean) {
        prefs.edit().putBoolean("${subjectName}_${chapterName}", isCompleted).apply()
        updateLastActiveDate()
    }

    fun isChapterCompleted(subjectName: String, chapterName: String): Boolean {
        return prefs.getBoolean("${subjectName}_${chapterName}", false)
    }

    fun getOverallProgress(): Int {
        val totalChapters = DataProvider.subjects.sumOf { it.chapters.size }
        if (totalChapters == 0) return 0
        var completedCount = 0
        for (subject in DataProvider.subjects) {
            for (chapter in subject.chapters) {
                if (isChapterCompleted(subject.name, chapter)) {
                    completedCount++
                }
            }
        }
        return (completedCount * 100) / totalChapters
    }

    fun getSubjectProgress(subjectName: String): Int {
        val subject = DataProvider.subjects.find { it.name == subjectName } ?: return 0
        val totalChapters = subject.chapters.size
        if (totalChapters == 0) return 0
        var completedCount = 0
        for (chapter in subject.chapters) {
            if (isChapterCompleted(subjectName, chapter)) {
                completedCount++
            }
        }
        return (completedCount * 100) / totalChapters
    }

    fun saveQuizScore(subjectName: String, score: Int) {
        val currentTotal = prefs.getInt("${subjectName}_quiz_total", 0)
        val currentCount = prefs.getInt("${subjectName}_quiz_count", 0)
        
        prefs.edit()
            .putInt("${subjectName}_quiz_total", currentTotal + score)
            .putInt("${subjectName}_quiz_count", currentCount + 1)
            .apply()
            
        updateLastActiveDate()
    }

    fun getAverageQuizScore(subjectName: String): Float {
        val currentTotal = prefs.getInt("${subjectName}_quiz_total", 0)
        val currentCount = prefs.getInt("${subjectName}_quiz_count", 0)
        if (currentCount == 0) return 0f
        // Assuming each quiz is out of 5, convert to percentage: (total / (count * 5)) * 100
        return (currentTotal.toFloat() / (currentCount * 5)) * 100f
    }

    private fun updateLastActiveDate() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        prefs.edit().putString("last_active_date", today).apply()
    }

    fun isActiveToday(): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        val lastActive = prefs.getString("last_active_date", "")
        return today == lastActive
    }
}
