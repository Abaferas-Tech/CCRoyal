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
    private lateinit var data: Map<Int,DataLoader.Item>

    init {
        getData()
    }

    override fun getData() {
        data = dataLoader.getData(Type.COMMON)
        iState.update {
            it.copy(
                currentSelected = 0,
                currentType = Type.COMMON
            )
        }
    }

    override fun onSelectType(index: Int, type: Type) {
        val result = isValid(state.value.userAmount, type)
        if (result.second && state.value.userAmount.isNotBlank()) {
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
                    )
                )
            }
        }
    }

    override fun onAmountChange(amount: String) {
        val result = isValid(amount, state.value.currentType)
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

    private fun isValid(amount: String, currentType: Type): Pair<String, Boolean> {
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

    override fun onClearAmount() {
        iState.update {
            it.copy(
                userAmount = ""
            )
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