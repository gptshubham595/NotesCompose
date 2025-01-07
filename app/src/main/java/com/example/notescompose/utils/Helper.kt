package com.example.notescompose.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.notescompose.features.notes.models.states.NotesStates
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.format

fun <R> handleNoteState(
    state: NotesStates<IFailure, R>,
    isDataLoading: MutableState<Boolean>,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    when {
        state.isLoading -> isDataLoading.value = true
        state.data != null && !state.isLoading -> {
            isDataLoading.value = false
            onSuccess()
        }

        state.error != null && !state.isLoading -> {
            isDataLoading.value = false
            onError()
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun formatTimeDifference(updatedAt: Long): String {
    val currentTime = Date()
    val diffInMillis = currentTime.time - updatedAt
    val diffInSeconds = diffInMillis / 1000
    val diffInMinutes = diffInSeconds / 60
    val diffInHours = diffInMinutes / 60
    val diffInDays = diffInHours / 24

    return when {
        diffInMinutes < 1 -> "Just now"
        diffInMinutes < 60 -> "$diffInMinutes minute${if (diffInMinutes > 1) "s" else ""} ago"
        diffInHours < 24 -> "$diffInHours hour${if (diffInHours > 1) "s" else ""} ago"
        diffInDays < 7 -> "$diffInDays day${if (diffInDays > 1) "s" else ""} ago"
        else -> SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(updatedAt)
    }
}