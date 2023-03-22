package com.example.inspirationpointtask.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.example.inspirationpointtask.ui.theme.InspirationPointTaskTheme

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val tableValues = viewModel.tableState.collectAsState().value
    val focusRequesterList = mutableListOf<FocusRequester>()
    Column(modifier = Modifier.fillMaxSize()) {
        for (i in 0..6) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (j in 0..6) {
                    val focusRequester = FocusRequester()
                    focusRequesterList.add(focusRequester)
                    TextField(
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = tableValues.cells[i][j],
                        onValueChange = { newValue: String ->
                            if (newValue.length <= 1) {
                                viewModel.updateTable(i, j, newValue)
                                val hasNextCell = j < 6 || i < 6
                                if (hasNextCell && newValue.length == 1 && newValue.isDigitsOnly() && newValue.toInt() <= 5) {
                                    focusRequesterList[i * 7 + j + 1].requestFocus()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InspirationPointTaskTheme {
        MainScreen(MainScreenViewModel())
    }
}