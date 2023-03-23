package com.example.inspirationpointtask.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inspirationpointtask.R
import com.example.inspirationpointtask.ui.theme.InspirationPointTaskTheme

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Box {
        Table(viewModel = viewModel)
    }
}

@Composable
fun Table(viewModel: MainScreenViewModel) {
    val tableValues = viewModel.tableState.collectAsState().value

    var participantWidth by remember { mutableStateOf(0) }
    var numbersWidth by remember { mutableStateOf(0) }
    var valuesWidth by remember { mutableStateOf(0) }
    var scoreWidth by remember { mutableStateOf(0) }
    var placeWidth by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
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
            ScoreColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scoreWidthInDp),
                calculateRowScore = { row ->
                    viewModel.calculateRowScore(row = row)
                }
            )
            val placeWidthInDp = with(LocalDensity.current) { placeWidth.toDp() }
            PlaceColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(placeWidthInDp),
                calculateRowPlace = { row ->
                    viewModel.calculateRowPlace(row)
                }
            )
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
            Text(text = stringResource(id = R.string.score))
        }
        TableCell(modifier = Modifier.onGloballyPositioned { placeWidthCallBack(it.size.width) }) {
            Text(text = stringResource(id = R.string.place))
        }
    }
}

@Composable
fun ParticipantsColumn(modifier: Modifier = Modifier, widthCallBack: (Int) -> Unit) {
    Column(modifier = modifier.onGloballyPositioned { widthCallBack(it.size.width) }) {
        for (i in 1..7) {
            TableCell(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = R.string.participant, i))
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
                    if (i == j) {
                        TableCell(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(MaterialTheme.colors.onBackground)
                        )
                    } else {
                        TableCell(modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)) {
                            CustomTextField(
                                focusRequester = focusRequester,
                                row = i,
                                column = j,
                                tableValues = tableValues,
                                updateTable = updateTable,
                                focusRequesterList = focusRequesterList
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    focusRequester: FocusRequester,
    row: Int,
    column: Int,
    tableValues: TableValues,
    updateTable: (Int, Int, String) -> Unit,
    focusRequesterList: List<FocusRequester>
) {
    val context = LocalContext.current

    val defaultColor = MaterialTheme.colors.onBackground
    val errorColor = MaterialTheme.colors.error
    var textColor by remember { mutableStateOf(defaultColor) }

    val unallowableToast = stringResource(id = R.string.unallowable_value)

    BasicTextField(
        modifier = Modifier
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            color = textColor,
            fontSize = MaterialTheme.typography.body1.fontSize,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = tableValues.cells[row][column],
        onValueChange = { newValue: String ->
            if (newValue.length <= 1) {
                updateTable(row, column, newValue)
                if (newValue.length == 1) {
                    if (valueIsAllowed(newValue)) {
                        textColor = defaultColor
                        val nextFocusRequesterIndex = calculateNextFocusRequesterIndex(
                            row = row,
                            column = column
                        )
                        if (nextFocusRequesterIndex in focusRequesterList.indices) {
                            focusRequesterList[nextFocusRequesterIndex].requestFocus()
                        }
                    } else {
                        textColor = errorColor
                        Toast.makeText(context, unallowableToast, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )
}

private fun calculateNextFocusRequesterIndex(row: Int, column: Int): Int {
    val nextRow = if (column < 6) row else row + 1
    val nextColumn = if (column < 6) column + 1 else 0
    return if (nextRow == nextColumn)
        calculateNextFocusRequesterIndex(row = nextRow, column = nextColumn)
    else
        nextRow * 7 + nextColumn
}

@Composable
fun ScoreColumn(
    modifier: Modifier = Modifier,
    calculateRowScore: (Int) -> Int
) {
    Column(modifier = modifier) {
        for (i in 0..6) {
            TableCell(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(text = calculateRowScore(i).toString())
            }
        }
    }
}

@Composable
fun PlaceColumn(
    modifier: Modifier = Modifier,
    calculateRowPlace: (Int) -> Int
) {
    Column(modifier = modifier) {
        for (i in 0..6) {
            TableCell(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(text = calculateRowPlace(i).toString())
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