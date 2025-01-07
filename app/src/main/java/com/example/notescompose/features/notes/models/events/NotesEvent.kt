package com.example.notescompose.features.notes.models.events

import com.example.notescompose.data.model.Note

sealed class NotesEvent {
    data class GetNotes(val morePages: Boolean = false, val pageSize: Int = 5) : NotesEvent()
    data class AddNote(val note: Note) : NotesEvent()
    data class DeleteNote(val id: Int) : NotesEvent()
    data class UpdateNote(val id: Int, val note: Note) : NotesEvent()
}