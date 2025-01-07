package com.example.notescompose.domain.repository

import com.example.notescompose.data.model.Note
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.utils.Either
import com.example.notescompose.utils.Failure
import com.example.notescompose.utils.IFailure
import kotlinx.coroutines.flow.Flow

interface NotesRepo {
    suspend fun getNotes(page: Int, pageSize: Int): Either<IFailure, Flow<List<NoteResponse>>>
    suspend fun addNote(note: Note): Either<Failure, Flow<NoteResponse>>
    suspend fun deleteNote(id: Int): Either<Failure, Flow<NoteResponse>>
    suspend fun updateNote(id: Int, note: Note): Either<Failure, Flow<NoteResponse>>
}