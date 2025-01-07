package com.example.notescompose.features.notes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.domain.repository.NotesRepo
import com.example.notescompose.features.notes.models.events.NotesEvent
import com.example.notescompose.features.notes.models.states.NotesStates
import com.example.notescompose.utils.Failure
import com.example.notescompose.utils.IFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesRepo: NotesRepo
) : ViewModel() {

    private var currentPage = 0
    private val pageSize = 5
    private var isLoadingMore = false
    private var hasMoreData = true

    private val _addNoteState = MutableStateFlow<NotesStates<IFailure, NoteResponse>?>(null)
    val addNoteState = _addNoteState.asStateFlow()

    private val _deleteNoteState =
        MutableStateFlow<NotesStates<IFailure, NoteResponse>?>(null)
    val deleteNoteState = _deleteNoteState.asStateFlow()

    private val _getNotesState =
        MutableStateFlow<NotesStates<IFailure, List<NoteResponse>>?>(null)
    val getNotesState = _getNotesState.asStateFlow()

    private val _updateNoteState =
        MutableStateFlow<NotesStates<IFailure, NoteResponse>?>(null)
    val updateNoteState = _updateNoteState.asStateFlow()

    init {
        onEvent(NotesEvent.GetNotes())
    }

    fun getCurrentNotes() {
        viewModelScope.launch {
            val data = notesRepo.getNotes(currentPage, pageSize)
            if (data.isSuccess()) {
                data.successValue()?.apply {
                    onStart {
                        _getNotesState.value = NotesStates(isLoading = true)
                    }.catch {
                        _getNotesState.value = NotesStates(
                            error = Failure.UnknownError(it.message ?: "An error occurred")
                        )
                    }.collect {
                        _getNotesState.value = NotesStates(data = it)
                    }
                }
            }
        }
    }

    fun loadMoreNotes() {
        if (isLoadingMore || !hasMoreData) return

        viewModelScope.launch {
            isLoadingMore = true
            val data = notesRepo.getNotes(currentPage, pageSize)
            if (data.isSuccess()) {
                data.successValue()?.apply {
                    onStart {
                        _getNotesState.value = NotesStates(isLoading = true)
                    }.catch {
                        _getNotesState.value = NotesStates(
                            error = Failure.UnknownError(it.message ?: "An error occurred")
                        )
                        isLoadingMore = false
                    }.collect { newNotes ->
                        if (newNotes.isEmpty()) {
                            hasMoreData = false
                        } else {
                            currentPage++
                        }
                        _getNotesState.value = NotesStates(data = newNotes)
                        isLoadingMore = false
                    }
                }
            }
        }
    }

    fun onEvent(event: NotesEvent) {
        Log.d("NoteViewModel", "onEvent: $event")
        when (event) {
            is NotesEvent.AddNote -> {
                viewModelScope.launch {
                    val data = notesRepo.addNote(event.note)
                    if (data.isSuccess()) {
                        data.successValue()?.apply {
                            onStart {
                                _addNoteState.value = NotesStates(isLoading = true)
                            }.catch {
                                _addNoteState.value = NotesStates(
                                    error = Failure.UnknownError(it.message ?: "An error occurred")
                                )
                            }.collect {
                                _addNoteState.value = NotesStates(data = it)
                            }
                        }
                    }
                }
            }


            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    val data = notesRepo.deleteNote(event.id)
                    if (data.isSuccess()) {
                        data.successValue()?.apply {
                            onStart {
                                _deleteNoteState.value = NotesStates(isLoading = true)
                            }.catch {
                                _deleteNoteState.value = NotesStates(
                                    error = Failure.UnknownError(it.message ?: "An error occurred"),
                                )
                            }.collect {
                                _deleteNoteState.value = NotesStates(data = it)
                            }
                        }
                    }
                }
            }

            is NotesEvent.GetNotes -> {
                if (!event.morePages) {
                    getCurrentNotes()
                } else {
                    loadMoreNotes()
                }
            }

            is NotesEvent.UpdateNote -> {
                viewModelScope.launch {
                    val data = notesRepo.updateNote(event.id, event.note)
                    if (data.isSuccess()) {
                        data.successValue()?.apply {
                            onStart {
                                _updateNoteState.value = NotesStates(isLoading = true)
                            }.catch {
                                _updateNoteState.value = NotesStates(
                                    error = Failure.UnknownError(it.message ?: "An error occurred"),
                                )
                            }.collect {
                                _updateNoteState.value = NotesStates(data = it)
                            }
                        }
                    }
                }
            }
        }


    }
}