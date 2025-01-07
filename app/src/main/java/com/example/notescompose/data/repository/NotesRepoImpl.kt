package com.example.notescompose.data.repository

import com.example.notescompose.data.model.Note
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.data.model.sampleNote
import com.example.notescompose.data.model.sampleNotes
import com.example.notescompose.data.network.ApiService
import com.example.notescompose.data.network.Datasource
import com.example.notescompose.domain.repository.NotesRepo
import com.example.notescompose.utils.Either
import com.example.notescompose.utils.Failure
import com.example.notescompose.utils.IFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class NotesRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val datasource: Datasource
) : NotesRepo {
    override suspend fun getNotes(
        page: Int,
        pageSize: Int
    ): Either<IFailure, Flow<List<NoteResponse>>> {
        return try {
            val allNotes = withContext(Dispatchers.IO) { datasource.getNotes() }

            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, allNotes.size)
            val paginatedNotes = if (startIndex < allNotes.size) {
                allNotes.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            Either.Success(flowOf(/*data*/paginatedNotes).flowOn(Dispatchers.IO))
//            if (result.isSuccessful) {
//                val data = result.body()
//                if (data != null) {
//                    Either.Success(flowOf(data).flowOn(Dispatchers.IO))
//                } else {
//                    Either.Error(Failure.NetworkError(Exception("No data found")))
//                }
//            } else {
//                Either.Error(Failure.NetworkError(Exception("Error fetching data")))
//            }
        } catch (e: Exception) {
            Either.Error(Failure.NetworkError(e))
        }
    }

    override suspend fun addNote(note: Note): Either<Failure, Flow<NoteResponse>> {
        return try {
            val result = withContext(Dispatchers.IO) { datasource.addNotes(note) }
            Either.Success(flowOf(/*data*/result).flowOn(Dispatchers.IO))
//            if (result.isSuccessful) {
//                val data = result.body()
//                if (data != null) {
//                    Either.Success(flowOf(/*data*/sampleNote()).flowOn(Dispatchers.IO))
//                } else {
//                    Either.Error(Failure.NetworkError(Exception("Something went wrong")))
//                }
//            } else {
//                Either.Error(Failure.NetworkError(Exception("Error Adding data")))
//            }
        } catch (e: Exception) {
            Either.Error(Failure.NetworkError(e))
        }
    }

    override suspend fun deleteNote(id: Int): Either<Failure, Flow<NoteResponse>> {
        return try {
            val result = withContext(Dispatchers.IO) { datasource.deleteNote(id) }
            result?.let {
                Either.Success(flowOf(/*data*/result).flowOn(Dispatchers.IO))
            } ?: run {
                Either.Error(Failure.NetworkError(Exception("Something went wrong")))
            }
//            if (result.isSuccessful) {
//                val data = result.body()
//                if (data != null) {
//                    Either.Success(flowOf(data).flowOn(Dispatchers.IO))
//                } else {
//                    Either.Error(Failure.NetworkError(Exception("Something went wrong")))
//                }
//            } else {
//                Either.Error(Failure.NetworkError(Exception("Error Deleting data")))
//            }
        } catch (e: Exception) {
            Either.Error(Failure.NetworkError(e))
        }
    }

    override suspend fun updateNote(
        id: Int,
        note: Note
    ): Either<Failure, Flow<NoteResponse>> {
        return try {
            val result = withContext(Dispatchers.IO) { datasource.updateNote(id, note) }
            result?.let {
                Either.Success(flowOf(/*data*/result).flowOn(Dispatchers.IO))
            } ?: run {
                Either.Error(Failure.NetworkError(Exception("Something went wrong")))
            }
//            if (result.isSuccessful) {
//                val data = result.body()
//                if (data != null) {
//                    Either.Success(flowOf(data).flowOn(Dispatchers.IO))
//                } else {
//                    Either.Error(Failure.NetworkError(Exception("Something went wrong")))
//                }
//            } else {
//                Either.Error(Failure.NetworkError(Exception("Error Updating data")))
//            }
        } catch (e: Exception) {
            Either.Error(Failure.NetworkError(e))
        }
    }
}