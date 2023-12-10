package com.abaferas.cardcalc.ui.screen

import com.abaferas.cardcalc.Type

interface HomeScreenInteraction {

    fun onSelectType(index: Int, type: Type)
    fun onAmountChange(amount: String)
    fun onCurrentLevelChange(level: String)
    fun onClearAmount()
    fun onClearLevel()

    fun onCalculateClick()

}