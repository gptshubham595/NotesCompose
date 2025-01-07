package com.example.notescompose.data.network

import com.example.notescompose.data.model.Note
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.data.model.sampleNotes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Datasource @Inject constructor() {
    private var currentId = 0
    val notesList = mutableListOf<NoteResponse>()

    init {
        (0..9).forEach {
            addNotes(Note("Title $it", "Description $it"))
        }
    }

    fun getNotes(
    ): List<NoteResponse> = notesList.toList()

    fun addNotes(
        note: Note
    ): NoteResponse {
        val newNote = NoteResponse(
            id = ++currentId,
            title = note.title,
            description = note.description,
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
        notesList.add(0,newNote)
        return newNote
    }

    fun deleteNote(
        id: Int
    ): NoteResponse? {
        val noteIndex = notesList.indexOfFirst { it.id == id }
        return if (noteIndex != -1) {
            notesList.removeAt(noteIndex)
        } else null
    }

    fun updateNote(
        id: Int,
        note: Note
    ): NoteResponse? {
        val noteIndex = notesList.indexOfFirst { it.id == id }
        if (noteIndex != -1) {
            val updatedNote = notesList[noteIndex].copy(
                title = note.title,
                description = note.description,
                updated_at = System.currentTimeMillis()
            )
            notesList.removeAt(noteIndex)
            notesList.add(0, updatedNote)
            return updatedNote
        }
        return null
    }
}