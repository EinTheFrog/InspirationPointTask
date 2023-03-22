package com.example.inspirationpointtask.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TableValues(val cells: Array<Array<String>>)

class MainScreenViewModel: ViewModel() {
    val tableState = MutableStateFlow(
        TableValues(Array(7) { Array(7) { "" } })
    )

    fun updateTable(row: Int, column: Int, value: String) {
        tableState.update {
            val newCells = tableState.value.cells
            newCells[row][column] = value
            return@update TableValues(newCells)
        }
    }

}