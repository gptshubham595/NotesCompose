package com.example.notescompose.features.notes.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notescompose.R
import com.example.notescompose.ui.theme.ContentColor
import com.example.notescompose.ui.theme.Orange

@Composable
fun ShowDialogBox(
    text: String,
    description: String,
    btnText: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onBtnClick: () -> Unit,
) {

    val focusRequester = FocusRequester()
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = {},
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        confirmButton = {
            Button(
                onClick = onBtnClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text(
                    btnText, style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        },
        dismissButton = {},
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onDismiss,
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Orange
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Row {
                    AppTextField(
                        text,
                        stringResource(R.string.title),
                        modifier = modifier.focusRequester(focusRequester),
                        onTitleChange,
                        backgroundColor = ContentColor
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    AppTextField(
                        description,
                        stringResource(R.string.description),
                        Modifier.height(300.dp),
                        onDescriptionChange,
                        backgroundColor = ContentColor
                    )
                }
            }

        }
    )
}

@Composable
fun AppTextField(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    backgroundColor: Color = Color.Transparent
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
        ),
        placeholder = {
            Text(
                placeholder,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black.copy(
                        alpha = 0.5f
                    ),
                    fontWeight = FontWeight.W500
                )
            )
        }

    )
}

@Preview(showBackground = true)
@Composable
fun ShowDialogBoxPreview() {
    ShowDialogBox("Title", "Description", "Save") {}
}
