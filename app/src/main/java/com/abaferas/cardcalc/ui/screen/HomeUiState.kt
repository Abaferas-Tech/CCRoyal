package com.abaferas.cardcalc.ui.screen

import com.abaferas.cardcalc.Type
import com.abaferas.cardcalc.ui.base.BaseUiState
import com.abaferas.cardcalc.ui.base.ErrorUiState


data class HomeUiState(
    val currentSelected: Int = 0,
    val userAmount: String = "",
    val amountError:ErrorUiState = ErrorUiState(),
    val currentLevel: String = "",
    val currentLevelError:ErrorUiState = ErrorUiState(),
    val currentType: Type = Type.NA,
    val maxLevel: String = "",
    val maxCards: String = "",
    val maxGold: String = "",
    val maxXp: String = "",
    val nextLevel: String = "",
    val needCards: String = "",
    val needGold: String = "",
    val xpGain: String = ""
) : BaseUiState