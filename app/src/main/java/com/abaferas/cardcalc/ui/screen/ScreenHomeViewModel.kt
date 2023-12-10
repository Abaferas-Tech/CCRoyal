package com.abaferas.cardcalc.ui.screen

import androidx.lifecycle.SavedStateHandle
import com.abaferas.cardcalc.Type
import com.abaferas.cardcalc.ui.base.BaseViewModel
import com.abaferas.cardcalc.ui.base.ErrorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScreenHomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    /*TODO Add you use cases*/
) : BaseViewModel<HomeUiState, HomeScreenUiEffect>(HomeUiState()), HomeScreenInteraction {

    private val args: HomeScreenArgs = HomeScreenArgs(savedStateHandle = savedStateHandle)
    private val dataLoader = DataLoader()
    private val data: Map<Int,DataLoader.Item> = dataLoader.getData(Type.COMMON)

    init {
        getData()
    }

    override fun getData() {
        iState.update {
            it.copy(
                currentSelected = 0,
                currentType = Type.COMMON
            )
        }
    }

    override fun onSelectType(index: Int, type: Type) {
        val result = amountValidation(state.value.userAmount, type)
        val levels = levelValidation(state.value.currentLevel,type)
        if (result.second && state.value.userAmount.isNotBlank() && levels.second) {
            iState.update {
                it.copy(
                    currentSelected = index,
                    currentType = type,
                    amountError = ErrorUiState()
                )
            }
        } else {
            iState.update {
                it.copy(
                    currentSelected = index,
                    currentType = type,
                    amountError = ErrorUiState(
                        isError = true,
                        message = "max: ${result.first}, type: ${state.value.currentType.label}."
                    ),
                    currentLevelError = ErrorUiState(
                        isError = true,
                        message = "range: ${levels.first}, type: ${state.value.currentType.label}."
                    )
                )
            }
        }
    }

    override fun onAmountChange(amount: String) {
        val result = amountValidation(amount, state.value.currentType)
        if (result.second && amount.isNotBlank()) {
            iState.update {
                it.copy(
                    userAmount = amount,
                    amountError = ErrorUiState()
                )
            }
        } else {
            iState.update {
                it.copy(
                    amountError = ErrorUiState(
                        true,
                        "max: ${result.first}, type: ${state.value.currentType.label}."
                    )
                )
            }
        }
    }

    override fun onCurrentLevelChange(level: String) {
        val result = levelValidation(level, state.value.currentType)
        if (result.second && level.isNotBlank()) {
            iState.update {
                it.copy(
                    currentLevel = level,
                    currentLevelError = ErrorUiState()
                )
            }
        } else {
            iState.update {
                it.copy(
                    currentLevelError = ErrorUiState(
                        isError = true,
                        message = "range: ${result.first}, type: ${state.value.currentType.label}."
                    )
                )
            }
        }
    }

    private fun amountValidation(amount: String, currentType: Type): Pair<String, Boolean> {
        if (amount.isBlank()) {
            return Pair("Enter amount", false)
        }
        val total = amount.toInt()
        return when (currentType) {
            Type.NA -> {
                Pair("", false)
            }

            Type.COMMON -> {
                Pair("12,087", total <= 12087)
            }

            Type.RARE -> {
                Pair("3287", total <= 3287)
            }

            Type.EPIC -> {
                Pair("427", total <= 427)
            }

            Type.LEGENDARY -> {
                Pair("43", total <= 43)
            }

            Type.CHAMPION -> {
                Pair("31", total <= 31)
            }
        }
    }
    private fun levelValidation(level: String, currentType: Type): Pair<String, Boolean> {
        if (level.isBlank()) {
            return Pair("Enter amount", false)
        }
        val total = level.toInt()
        return when (currentType) {
            Type.NA -> {
                Pair("", false)
            }

            Type.COMMON -> {
                Pair("Lvl: 1 : 14", total in 1..14)
            }

            Type.RARE -> {
                Pair("Lvl: 3 : 14", total in 3..14)
            }

            Type.EPIC -> {
                Pair("Lvl: 6 : 14", total in 6..14)
            }

            Type.LEGENDARY -> {
                Pair("Lvl: 9 : 14", total in 9..14)
            }

            Type.CHAMPION -> {
                Pair("Lvl: 11 : 14", total in 11..14)
            }
        }
    }

    override fun onClearAmount() {
        iState.update {
            it.copy(
                userAmount = ""
            )
        }
    }

    override fun onClearLevel() {
        iState.update {
            it.copy(
                currentLevel = ""
            )
        }
    }

    override fun onCalculateClick() {
        val isLevelValid = levelValidation(state.value.currentLevel, state.value.currentType).second
        val isAmountValid = amountValidation(state.value.userAmount, state.value.currentType).second
        if (isAmountValid && isLevelValid){
            val nextLevel = calculateNextLevel(state.value.currentLevel.toInt(),state.value.userAmount.toInt(),dataLoader.getData(state.value.currentType))
            val neededCards = calculateNeededCards(state.value.currentLevel.toInt(),state.value.userAmount.toInt(),dataLoader.getData(state.value.currentType))
            iState.update {
                it.copy(
                    nextLevel = "$nextLevel",
                    maxLevel = "14",
                    maxCards = "${dataLoader.getData(state.value.currentType)[nextLevel]!!.cards}",
                    needCards = "$neededCards"
                )
            }
        }else {
            iState.update {
                it.copy(
                    amountError = ErrorUiState(
                        true,
                        "max: ${state.value.userAmount}, type: ${state.value.currentType.label}."
                    ),
                    currentLevelError = ErrorUiState(
                        isError = true,
                        message = "range: ${state.value.currentLevel}, type: ${state.value.currentType.label}."
                    )
                )
            }
        }
    }

    private fun calculateNextLevel(level: Int,amount: Int,dataSet: Map<Int,DataLoader.Item>): Int {
        return if (amount < dataSet[level]!!.cards) amount
        else {
            var next = level
            var total = amount
            while (total > dataSet[next]!!.cards){
                total -= dataSet[next]!!.cards
                next++
            }
            next
        }
    }

    private fun calculateNeededCards(level: Int,amount: Int,dataSet: Map<Int, DataLoader.Item>): Int{
        var currentAmount = amount
        var currentLevel = level
        var neededAmount = 0
        val nextLvl = calculateNextLevel(level, amount, dataSet)
        if (amount < dataSet[level]!!.cards) return dataSet[level]?.cards!! - amount
        else {
            while (currentAmount >= dataSet[currentLevel]?.cards!!){
                currentAmount -= dataSet[currentLevel]?.cards!!
                currentLevel++
            }
            return currentAmount
        }
    }
}

class DataLoader {

    fun getData(type: Type): Map<Int, Item> {
        return when (type) {
            Type.NA -> emptyMap()
            Type.COMMON -> data.common
            Type.RARE -> data.rare
            Type.EPIC -> data.epic
            Type.LEGENDARY -> data.legendary
            Type.CHAMPION -> data.champ
        }
    }


    data class DataSet(
        val common: Map<Int, Item>,
        val rare: Map<Int, Item>,
        val epic: Map<Int, Item>,
        val legendary: Map<Int, Item>,
        val champ: Map<Int, Item>,
    )

    data class Item(
        val cards: Int,
        val gold: Int,
        val xp: Int,
    )

    val data = DataSet(
        common = mapOf(
            1 to Item(1, 0, 0),
            2 to Item(2, 5, 4),
            3 to Item(4, 20, 5),
            4 to Item(10, 50, 6),
            5 to Item(20, 150, 10),
            6 to Item(50, 400, 25),
            7 to Item(100, 1000, 50),
            8 to Item(200, 2000, 100),
            9 to Item(400, 4000, 200),
            10 to Item(800, 8000, 400),
            11 to Item(1000, 15000, 600),
            12 to Item(1500, 35000, 800),
            13 to Item(3000, 75000, 1600),
            14 to Item(5000, 100000, 2000),
        ),
        rare = mapOf(
            3 to Item(1, 0, 0),
            4 to Item(2, 50, 6),
            5 to Item(4, 150, 10),
            6 to Item(10, 400, 25),
            7 to Item(20, 1000, 50),
            8 to Item(50, 2000, 100),
            9 to Item(100, 4000, 200),
            10 to Item(200, 8000, 400),
            11 to Item(400, 15000, 600),
            12 to Item(500, 35000, 800),
            13 to Item(750, 75000, 1600),
            14 to Item(1250, 100000, 2000),
        ),
        epic = mapOf(
            6 to Item(1, 0, 0),
            7 to Item(2, 400, 25),
            8 to Item(4, 2000, 100),
            9 to Item(10, 4000, 200),
            10 to Item(20, 8000, 400),
            11 to Item(40, 15000, 600),
            12 to Item(50, 35000, 800),
            13 to Item(100, 75000, 1600),
            14 to Item(200, 100000, 2000),
        ),
        legendary = mapOf(
            9 to Item(1, 0, 0),
            10 to Item(2, 5000, 250),
            11 to Item(4, 15000, 600),
            12 to Item(6, 35000, 800),
            13 to Item(10, 75000, 1600),
            14 to Item(20, 100000, 2000),
        ),
        champ = mapOf(
            11 to Item(1, 0, 800),
            12 to Item(2, 35000, 1600),
            13 to Item(8, 75000, 2000),
            14 to Item(20, 100000, 4400),
        ),
    )
}