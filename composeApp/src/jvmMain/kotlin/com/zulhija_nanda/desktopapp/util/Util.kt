package com.zulhija_nanda.desktopapp.util

object Util {

    fun formatTimestamp(timestamp: String): String {
        return try {
            // Simple formatting - adjust based on your date format
            val parts = timestamp.split("T")
            if (parts.isNotEmpty()) parts[0] else timestamp
        } catch (e: Exception) {
            timestamp
        }
    }

}