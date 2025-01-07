package com.example.notescompose.features.notes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notescompose.data.model.NoteResponse
import com.example.notescompose.ui.theme.ContentColor
import com.example.notescompose.ui.theme.Orange
import com.example.notescompose.utils.formatTimeDifference

@Composable
fun NotesEachRow(
    noteResponse: NoteResponse,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onViewClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onViewClick)
            .background(ContentColor, shape = RoundedCornerShape(8.dp)),
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = noteResponse.title,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(0.7f),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W600
                    ),
                    overflow = TextOverflow.Ellipsis, // Add this line
                    maxLines = 1
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(0.3f)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Orange
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = noteResponse.description,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                    style = TextStyle(
                        color = Color.Black.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    ),
                    overflow = TextOverflow.Ellipsis, // Add this line
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formatTimeDifference(noteResponse.updated_at),
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                    )
                )
            }
        }
    }
}
