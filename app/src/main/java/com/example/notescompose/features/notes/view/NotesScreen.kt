package com.example.notescompose.features.notes.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notescompose.data.model.Note
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.features.notes.models.events.NotesEvent
import com.example.notescompose.features.notes.viewmodel.NoteViewModel
import com.example.notescompose.ui.theme.Orange
import com.example.notescompose.utils.handleNoteState
import com.example.notescompose.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotesScreen(
    viewModel: NoteViewModel = hiltViewModel()
) {

    val noteResponse = viewModel.getNotesState.collectAsState()

    val search = rememberSaveable { mutableStateOf("") }
    var title = rememberSaveable { mutableStateOf("") }
    var noteId = rememberSaveable { mutableIntStateOf(0) }
    var description = rememberSaveable { mutableStateOf("") }
    var isNotesAddDialogVisible = rememberSaveable { mutableStateOf(false) }
    var isNotesUpdateDialogVisible = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val isDataLoading: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.addNoteState.collectLatest { noteState ->
            noteState?.let {
                handleNoteState<NoteResponse>(
                    state = noteState,
                    isDataLoading = isDataLoading,
                    onSuccess = {
                        isNotesAddDialogVisible.value = false
                        showToast(context, "Note added successfully")
                        viewModel.onEvent(NotesEvent.GetNotes())
                    },
                    onError = {
                        isNotesAddDialogVisible.value = false
                        showToast(context, "Failed to add note")
                    }
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getNotesState.collectLatest { noteState ->
            noteState?.let {
                handleNoteState<List<NoteResponse>>(
                    state = noteState,
                    isDataLoading = isDataLoading,
                    onSuccess = { showToast(context, "Fetched notes successfully") },
                    onError = { showToast(context, "Failed to fetch notes") }
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.deleteNoteState.collectLatest { noteState ->
            noteState?.let {
                handleNoteState<NoteResponse>(
                    state = noteState,
                    isDataLoading = isDataLoading,
                    onSuccess = {
                        showToast(context, "Note deleted successfully")
                        viewModel.onEvent(NotesEvent.GetNotes())
                    },
                    onError = { showToast(context, "Failed to delete note") }
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.updateNoteState.collectLatest { noteState ->
            noteState?.let {
                handleNoteState<NoteResponse>(
                    state = noteState,
                    isDataLoading = isDataLoading,
                    onSuccess = {
                        showToast(context, "Note updated successfully")
                        isNotesUpdateDialogVisible.value = false
                        viewModel.onEvent(NotesEvent.GetNotes())
                    },
                    onError = {
                        showToast(context, "Failed to update note")
                        isNotesUpdateDialogVisible.value = false
                    }

                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isNotesAddDialogVisible.value = true },
                shape = CircleShape,
                containerColor = Orange,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppSearchBar(
                search = search.value,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp),
                onValueChange = { it ->
                    search.value = it
                },
                onClear = { search.value = "" },
            )

            noteResponse.value?.data?.let { list: List<NoteResponse> ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val filteredList = if (search.value.isEmpty()) {
                        list
                    } else {
                        list.filter {
                            it.title.lowercase().contains(
                                search.value,
                                ignoreCase = true
                            ) || it.description
                                .contains(search.value, ignoreCase = true)
                        }
                    }
                    items(filteredList, key = { it.id }) { noteResponse ->
                        NotesEachRow(
                            modifier = Modifier,
                            noteResponse = noteResponse,
                            onDelete = {
                                viewModel.onEvent(NotesEvent.DeleteNote(noteResponse.id))
                            },
                            onViewClick = {
                                title.value = noteResponse.title
                                description.value = noteResponse.description
                                noteId.intValue = noteResponse.id
                                isNotesUpdateDialogVisible.value = true
                            }
                        )
                    }
                }
            }

        }
        if (isDataLoading.value) {
            LoadingDialog()
        }
        if (isNotesAddDialogVisible.value || isNotesUpdateDialogVisible.value) {
            ShowDialogBox(
                text = title.value,
                description = description.value,
                btnText = if (isNotesAddDialogVisible.value) "Save" else "Update",
                onDismiss = {
                    if (isNotesAddDialogVisible.value) {
                        isNotesAddDialogVisible.value = false
                    } else {
                        isNotesUpdateDialogVisible.value = false
                    }
                },
                onTitleChange = { title.value = it },
                onDescriptionChange = { description.value = it },
                onBtnClick = {
                    val note = Note(
                        title = title.value,
                        description = description.value
                    )
                    if (isNotesUpdateDialogVisible.value) {
                        viewModel.onEvent(
                            NotesEvent.UpdateNote(
                                id = noteId.intValue,
                                note = note
                            )
                        )
                    } else {
                        viewModel.onEvent(
                            NotesEvent.AddNote(
                                note = Note(
                                    title = title.value,
                                    description = description.value
                                )
                            )
                        )
                    }

                    title.value = ""
                    description.value = ""
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen()
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator(color = Orange)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoadingDialogPreview() {
//    LoadingDialog()
//}