package com.abaferas.cardcalc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abaferas.cardcalc.ui.screen.ScreenHome
import com.abaferas.cardcalc.ui.theme.CardCalcTheme
import com.abaferas.cardcalc.ui.theme.color_champion
import com.abaferas.cardcalc.ui.theme.color_common
import com.abaferas.cardcalc.ui.theme.color_epic
import com.abaferas.cardcalc.ui.theme.color_legendary
import com.abaferas.cardcalc.ui.theme.color_rare
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenHome()
                }
            }
        }
    }
}

@Preview
@Composable
fun Tester() {

}

enum class Type(
    val base: Int,
    val label: String,
    val color: Color
) {
    NA(base = 0,"", Color.Transparent),
    COMMON(base = 1,"Common", color_common),
    RARE(base = 3,"Rare", color_rare),
    EPIC(base = 6,"Epic", color_epic),
    LEGENDARY(base = 9,"Legendary", color_legendary),
    CHAMPION(base = 11,"Champion", color_champion)
}

data class Cards(
    val level: Int,
    val common: Int,
    val rare: Int,
    val epic: Int,
    val legendary: Int,
    val champion: Int,
)

data class Gold(
    val level: Int,
    val common: Int,
    val rare: Int,
    val epic: Int,
    val legendary: Int,
    val champion: Int,
)

data class Xp(
    val level: Int,
    val common: Int,
    val rare: Int,
    val epic: Int,
    val legendary: Int,
    val champion: Int,
)