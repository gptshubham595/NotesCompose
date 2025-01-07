package com.example.notescompose.data.model

data class Note(
    val title: String,
    val description: String,
)

data class NoteResponse(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: Long,
    val updated_at: Long,
)

fun sampleNotes() = List<NoteResponse>(
    10,
    init = {
        NoteResponse(
            id = it,
            title = "Title $it",
            description = "Description $it",
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
)


fun sampleNote() = NoteResponse(
    id = 9,
    title = "Title NEW",
    description = "Description NEW",
    created_at = System.currentTimeMillis(),
    updated_at = System.currentTimeMillis()
)

