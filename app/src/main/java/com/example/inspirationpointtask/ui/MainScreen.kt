package com.example.inspirationpointtask.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.inspirationpointtask.ui.theme.InspirationPointTaskTheme

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val tableValues = viewModel.tableState.collectAsState().value

    var participantWidth by remember { mutableStateOf(0) }
    var numbersWidth by remember { mutableStateOf(0) }
    var valuesWidth by remember { mutableStateOf(0) }
    var scoreWidth by remember { mutableStateOf(0) }
    var placeWidth by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitlesRow(
            participantWidth = participantWidth,
            numbersWidth = numbersWidth,
            valuesWidthCallBack = { if (it > valuesWidth) valuesWidth = it },
            scoreWidthCallBack = { if (it > scoreWidth) scoreWidth = it },
            placeWidthCallBack = { if (it > placeWidth) placeWidth = it }
        )
        Row {
            ParticipantsColumn(
                modifier = Modifier.fillMaxHeight(),
                widthCallBack = { if (it > participantWidth) participantWidth = it }
            )
            NumbersColumn(
                modifier = Modifier.fillMaxHeight(),
                widthCallBack = { if (it > numbersWidth) numbersWidth = it }
            )

            val valuesWidthInDp = with(LocalDensity.current) { valuesWidth.toDp() }
            ValuesTable(
                modifier = Modifier.width(valuesWidthInDp),
                tableValues = tableValues,
                updateTable = viewModel::updateTable
            )

            val scoreWidthInDp = with(LocalDensity.current) { scoreWidth.toDp() }
            ScoreColumn(modifier = Modifier
                .fillMaxHeight()
                .width(scoreWidthInDp))
            val placeWidthInDp = with(LocalDensity.current) { placeWidth.toDp() }
            PlaceColumn(modifier = Modifier
                .fillMaxHeight()
                .width(placeWidthInDp))
        }
    }

}

@Composable
fun TitlesRow(
    modifier: Modifier = Modifier,
    participantWidth: Int,
    numbersWidth: Int,
    valuesWidthCallBack: (Int) -> Unit,
    scoreWidthCallBack: (Int) -> Unit,
    placeWidthCallBack: (Int) -> Unit
) {
    val participantWidthInDp = with(LocalDensity.current) { participantWidth.toDp() }
    val numbersWidthInDp = with(LocalDensity.current) { numbersWidth.toDp() }

    Row(modifier = modifier) {
        TableCell(modifier = Modifier.width(participantWidthInDp)) {
            Text(text = "")
        }
        TableCell(modifier = Modifier.width(numbersWidthInDp)) {
            Text(text = "")
        }
        Row(
            modifier = Modifier.onGloballyPositioned { valuesWidthCallBack(it.size.width) }
        ) {
            for (i in 1..7) {
                TableCell {
                    Text(modifier = Modifier.padding(horizontal = 12.dp), text = "$i")
                }
            }
        }
        TableCell(modifier = Modifier.onGloballyPositioned { scoreWidthCallBack(it.size.width) }) {
            Text(text = "Сумма очков")
        }
        TableCell(modifier = Modifier.onGloballyPositioned { placeWidthCallBack(it.size.width) }) {
            Text(text = "Место")
        }
    }
}

@Composable
fun ParticipantsColumn(modifier: Modifier = Modifier, widthCallBack: (Int) -> Unit) {
    Column(modifier = modifier.onGloballyPositioned { widthCallBack(it.size.width) }) {
        for (i in 1..7) {
            TableCell(modifier = Modifier.weight(1f)) {
                Text(text = "Участник $i")
            }
        }
    }
}

@Composable
fun NumbersColumn(modifier: Modifier = Modifier, widthCallBack: (Int) -> Unit) {
    Column(modifier = modifier.onGloballyPositioned { widthCallBack(it.size.width) }) {
        for (i in 1..7) {
            TableCell(modifier = Modifier.weight(1f)) {
                Text(text = "$i")
            }
        }
    }
}

@Composable
fun ValuesTable(
    modifier: Modifier = Modifier,
    tableValues: TableValues,
    updateTable: (Int, Int, String) -> Unit
) {
    val focusRequesterList = mutableListOf<FocusRequester>()
    Column(modifier = modifier) {
        for (i in 0..6) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (j in 0..6) {
                    val focusRequester = FocusRequester()
                    focusRequesterList.add(focusRequester)
                    TableCell(modifier = Modifier.fillMaxHeight().weight(1f)) {
                        BasicTextField(
                            modifier = Modifier
                                .focusRequester(focusRequester),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = MaterialTheme.typography.body1.fontSize,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = tableValues.cells[i][j],
                            onValueChange = { newValue: String ->
                                if (newValue.length <= 1) {
                                    updateTable(i, j, newValue)
                                    val hasNextCell = j < 6 || i < 6
                                    if (
                                        hasNextCell
                                        && newValue.length == 1
                                        && newValue.isDigitsOnly()
                                        && newValue.toInt() <= 5
                                    ) {
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
}

@Composable
fun ScoreColumn(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        for (i in 1..7) {
            TableCell(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(text = "")
            }
        }
    }
}

@Composable
fun PlaceColumn(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        for (i in 1..7) {
            TableCell(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(text = "")
            }
        }
    }
}

@Composable
fun TableCell(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
        .border(1.dp, MaterialTheme.colors.onBackground)
        .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InspirationPointTaskTheme {
        MainScreen(MainScreenViewModel())
    }
}