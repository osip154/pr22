package com.Osipov.Pr22_Osipov

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Osipov.Pr22_Osipov.model.MemoryCard
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MemoryApp(this)
        }
    }
}

@Composable
fun MemoryApp(context: Context) {

    var screen by remember {
        mutableStateOf("start")
    }

    var backgroundColor by remember {
        mutableStateOf(Color(0xFFEFEFEF))
    }

    var bestRecord by remember {
        mutableIntStateOf(loadRecord(context))
    }

    when (screen) {

        "start" -> {
            StartScreen(
                onStart = {
                    screen = "game"
                },
                onSettings = {
                    screen = "settings"
                },
                onRecords = {
                    screen = "records"
                }
            )
        }

        "settings" -> {
            SettingsScreen(
                onBack = {
                    screen = "start"
                },
                onColorChange = {
                    backgroundColor = it
                }
            )
        }

        "records" -> {
            RecordScreen(
                bestRecord = bestRecord,
                onBack = {
                    screen = "start"
                }
            )
        }

        "game" -> {
            GameScreen(
                backgroundColor = backgroundColor,
                onBack = {
                    screen = "start"
                },
                onNewRecord = {

                    if (it < bestRecord) {

                        bestRecord = it

                        saveRecord(context, it)
                    }
                }
            )
        }
    }
}

@Composable
fun StartScreen(
    onStart: () -> Unit,
    onSettings: () -> Unit,
    onRecords: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Найди пару",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onStart) {
            Text("Начать игру")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onSettings) {
            Text("Настройки")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onRecords) {
            Text("Рекорды")
        }
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onColorChange: (Color) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Настройки",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onColorChange(Color.White)
            }
        ) {
            Text("Белый фон")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                onColorChange(Color.LightGray)
            }
        ) {
            Text("Серый фон")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                onColorChange(Color.Cyan)
            }
        ) {
            Text("Голубой фон")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun RecordScreen(
    bestRecord: Int,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Рекорд",
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "$bestRecord ходов",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun GameScreen(
    backgroundColor: Color,
    onBack: () -> Unit,
    onNewRecord: (Int) -> Unit
) {

    val images = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6,
        R.drawable.img7,
        R.drawable.img8,
        R.drawable.img9,
        R.drawable.img10,
        R.drawable.img11,
        R.drawable.img12,
        R.drawable.img13,
        R.drawable.img14,
        R.drawable.img15,
        R.drawable.img16,
        R.drawable.img17,
        R.drawable.img18
    )

    var cards by remember {

        mutableStateOf(
            (images + images)
                .shuffled()
                .map {
                    MemoryCard(it)
                }
        )
    }

    var selectedCards by remember {
        mutableStateOf(listOf<Int>())
    }

    var moves by remember {
        mutableIntStateOf(0)
    }

    val gameFinished =
        cards.all { it.isMatched }

    LaunchedEffect(selectedCards) {

        if (selectedCards.size == 2) {

            delay(1000)

            val first = selectedCards[0]
            val second = selectedCards[1]

            if (
                cards[first].imageRes ==
                cards[second].imageRes
            ) {

                val updated =
                    cards.toMutableList()

                updated[first] =
                    updated[first].copy(
                        isMatched = true
                    )

                updated[second] =
                    updated[second].copy(
                        isMatched = true
                    )

                cards = updated

            } else {

                val updated =
                    cards.toMutableList()

                updated[first] =
                    updated[first].copy(
                        isOpened = false
                    )

                updated[second] =
                    updated[second].copy(
                        isOpened = false
                    )

                cards = updated
            }

            selectedCards = emptyList()
        }
    }

    if (gameFinished) {
        onNewRecord(moves)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "Ходы: $moves",
                fontSize = 24.sp
            )

            Button(onClick = onBack) {
                Text("Меню")
            }
        }

        if (gameFinished) {

            Text(
                text = "Игра окончена!",
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.padding(4.dp),
            verticalArrangement =
                Arrangement.spacedBy(4.dp),
            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            itemsIndexed(cards) { index, card ->

                Card(
                    modifier = Modifier
                        .size(56.dp)
                        .clickable {

                            if (
                                !card.isOpened &&
                                !card.isMatched &&
                                selectedCards.size < 2
                            ) {

                                val updated =
                                    cards.toMutableList()

                                updated[index] =
                                    updated[index].copy(
                                        isOpened = true
                                    )

                                cards = updated

                                selectedCards =
                                    selectedCards + index

                                if (
                                    selectedCards.size == 1
                                ) {
                                    moves++
                                }
                            }
                        }
                ) {

                    if (
                        card.isOpened ||
                        card.isMatched
                    ) {

                        Image(
                            painter = painterResource(
                                card.imageRes
                            ),
                            contentDescription = null
                        )

                    } else {

                        Image(
                            painter = painterResource(
                                R.drawable.back
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

fun saveRecord(
    context: Context,
    record: Int
) {

    val prefs =
        context.getSharedPreferences(
            "memory",
            Context.MODE_PRIVATE
        )

    prefs.edit()
        .putInt("record", record)
        .apply()
}

fun loadRecord(
    context: Context
): Int {

    val prefs =
        context.getSharedPreferences(
            "memory",
            Context.MODE_PRIVATE
        )

    return prefs.getInt("record", 999)
}
