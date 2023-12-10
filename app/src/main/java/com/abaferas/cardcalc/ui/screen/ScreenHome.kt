package com.abaferas.cardcalc.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abaferas.cardcalc.R
import com.abaferas.cardcalc.Type
import com.abaferas.cardcalc.ui.theme.Athiti


@Composable
fun ScreenHome(
    screenHomeViewModel: ScreenHomeViewModel = hiltViewModel()
) {
    val state = screenHomeViewModel.state.collectAsState().value
    ScreenHomeContent(state = state, interaction = screenHomeViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHomeContent(
    state: HomeUiState,
    interaction: HomeScreenInteraction
) {
    val types = listOf(Type.COMMON, Type.RARE, Type.EPIC, Type.LEGENDARY, Type.CHAMPION)

    LazyColumn(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CenterAlignedTopAppBar(
                title = { Text(text = "Calculator") },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(types.size) {
                    Card(
                        modifier = Modifier.size(150.dp),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        border = BorderStroke(
                            width = if (it == state.currentSelected) 2.dp else 0.dp,
                            color = if (it == state.currentSelected) Color.Black else Color.Transparent
                        ),
                        onClick = {
                            interaction.onSelectType(it, types[it])
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = types[it].color
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center,
                                text = types[it].label,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = Athiti
                            )
                        }
                    }
                }
            }
        }

        item {
            DataRow(
                "Next:" , "${state.nextLevel}",state.currentType.color
            )
        }
        item {
            DataRow(
                "Cards" , "${state.needCards}",state.currentType.color
            )
        }

        item {
            DataRow(
                "Gold" , "${state.needGold}",state.currentType.color
            )
        }

        item {
            DataRow(
                "Xp Gain:" , "${state.xpGain}",state.currentType.color
            )
        }


        item {
            TextField(
                modifier = Modifier.padding(top = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = state.currentType.color,
                    cursorColor = state.currentType.color,
                    unfocusedIndicatorColor = state.currentType.color,
                    focusedTextColor = state.currentType.color,
                    unfocusedTextColor = state.currentType.color,
                    focusedLeadingIconColor = state.currentType.color,
                    unfocusedLeadingIconColor = state.currentType.color,
                    focusedTrailingIconColor = state.currentType.color,
                    unfocusedTrailingIconColor = state.currentType.color,
                    focusedSupportingTextColor = Color.Red,
                    unfocusedSupportingTextColor = Color.Red
                ),
                supportingText = {
                    AnimatedVisibility(visible = state.currentLevelError.isError) {
                        Text(text = state.currentLevelError.message, fontWeight = FontWeight.Medium)
                    }
                },
                singleLine = true, maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                value = if (state.currentLevel == 0) "" else "${state.currentLevel}", onValueChange ={
                    val con = if (it.isBlank()) 0 else it.toInt()
                    interaction.onCurrentLevelChange(con)
                },
                placeholder = { Text(text = "Current Level", color = state.currentType.color) },
                leadingIcon = {
                    Icon(
                        tint = state.currentType.color,
                        painter = painterResource(id = R.drawable.layers),
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = "${state.currentLevel}".isNotBlank()) {
                        Icon(
                            modifier = Modifier.clickable { interaction.onClearLevel() },
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = ""
                        )
                    }
                }
            )
        }

        item {
            TextField(
                modifier = Modifier.padding(top = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = state.currentType.color,
                    cursorColor = state.currentType.color,
                    unfocusedIndicatorColor = state.currentType.color,
                    focusedTextColor = state.currentType.color,
                    unfocusedTextColor = state.currentType.color,
                    focusedLeadingIconColor = state.currentType.color,
                    unfocusedLeadingIconColor = state.currentType.color,
                    focusedTrailingIconColor = state.currentType.color,
                    unfocusedTrailingIconColor = state.currentType.color,
                    focusedSupportingTextColor = Color.Red,
                    unfocusedSupportingTextColor = Color.Red
                ),
                supportingText = {
                    AnimatedVisibility(visible = state.amountError.isError) {
                        Text(text = state.amountError.message, fontWeight = FontWeight.Medium)
                    }
                },
                singleLine = true, maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                value = if(state.userAmount == 0) "" else "${state.userAmount}",
                onValueChange = {
                    val con = if (it.isBlank()) 0 else it.toInt()
                    interaction.onAmountChange(con)
                }
                ,
                placeholder = { Text(text = "Amount", color = state.currentType.color) },
                leadingIcon = {
                    Icon(
                        tint = state.currentType.color,
                        painter = painterResource(id = R.drawable.layers),
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = "${state.userAmount}".isNotBlank()) {
                        Icon(
                            modifier = Modifier.clickable { interaction.onClearAmount() },
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = ""
                        )
                    }
                }
            )
        }

        item{
            Button(onClick = { interaction.onCalculateClick() }) {
                Text(text = "Calculate")
            }
        }
    }
}

@Composable
fun DataRow(
    label: String,
    result: String,
    color: Color = Color.Yellow,
    paddingValues: PaddingValues = PaddingValues(top = 8.dp, start = 32.dp,end = 32.dp)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(textAlign = TextAlign.Start, text = label, fontSize = 20.sp, fontFamily = Athiti, fontWeight = FontWeight.Bold, color = color)
        Text(textAlign = TextAlign.End,text = result, fontSize = 20.sp, fontFamily = Athiti, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}
