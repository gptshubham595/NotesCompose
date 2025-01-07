package com.example.notescompose.utils

interface IFailure

sealed class Failure : IFailure {
    data class NetworkError(val message: Exception) : Failure()
    data class UnknownError(val message: String) : Failure()
}