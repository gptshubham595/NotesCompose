package com.example.notescompose.features.notes.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.ui.theme.ContentColor
import com.example.notescompose.ui.theme.Orange

@Composable
fun AppSearchBar(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onClear: () -> Unit = {}
) {
    TextField(
        value = search,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Gray,
            focusedContainerColor = ContentColor,
            unfocusedContainerColor = ContentColor,
        ),
        placeholder = {
            Text(
                "Search Notes...", style =
                    TextStyle(
                        color = Color.Black.copy(alpha = 0.5f)
                    )
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Orange
            )
        },
        trailingIcon = {
            if (search.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NotesEachRowPreview() {
    NotesEachRow(
        noteResponse = NoteResponse(
            id = 1,
            title = "Title",
            description = "Description",
            created_at = 0,
            updated_at = 0
        )
    )
}
