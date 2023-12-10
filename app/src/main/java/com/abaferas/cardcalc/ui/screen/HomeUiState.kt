package com.abaferas.cardcalc.ui.screen

import com.abaferas.cardcalc.Type
import com.abaferas.cardcalc.ui.base.BaseUiState
import com.abaferas.cardcalc.ui.base.ErrorUiState


data class HomeUiState(
    val currentSelected: Int = 0,
    val userAmount: Int = 0,
    val amountError:ErrorUiState = ErrorUiState(),
    val currentLevel: Int = 0,
    val currentLevelError:ErrorUiState = ErrorUiState(),
    val currentType: Type = Type.COMMON,
    val nextLevel: Int = 0,
    val needCards: Int = 0,
    val needGold: Int = 0,
    val xpGain: Int = 0,
) : BaseUiState