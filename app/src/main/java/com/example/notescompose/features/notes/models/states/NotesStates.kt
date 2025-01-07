package com.example.notescompose.features.notes.models.states

import com.example.notescompose.utils.IFailure

data class NotesStates<out L : IFailure, out R>(
    val error: L? = null,
    val data: R? = null,
    val isLoading: Boolean = true
) {
    constructor(error: L) : this(error, null, false)
    constructor(data: R) : this(null, data, false)
}