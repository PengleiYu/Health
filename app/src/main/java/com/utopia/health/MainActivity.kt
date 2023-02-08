package com.utopia.health

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utopia.health.ui.theme.HealthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    CalculateContent()
//                }
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = "热量计算器") }
                    )
                }) {
                    Box(modifier = Modifier.padding(it)) {
                        CalculateContent()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

private const val TAG = "MainActivity"

@Composable
fun CalculateContent() {
    Column {
        var grams by remember { mutableStateOf(0f) }
        var kJ by remember { mutableStateOf(0f) }
        var weight by remember { mutableStateOf(0f) }
//        LabelLine(
//            label = "100g热量",
//            count = 0f,
//            tail = "千焦",
//            onValueChange = { kJ = it.toFloatOrNull() ?: 0f })
        CaloriePerGramRow(
            onValueChange = { _grams, _kCalorie ->
                grams = _grams
                kJ = _kCalorie
            }
        )

        val kK = if (grams == 0f) 0f else (kJ / grams * weight * 0.238846f)
        val minutes = kK / 10f

        Log.d(TAG, "CalContent: kK=$kK, minutes=$minutes")
        LabelLine(
            label = "重量",
            count = 100f,
            tail = "g",
            onValueChange = { weight = it.toFloatOrNull() ?: 0f })
        LabelLine(label = "千卡", count = kK, tail = "")
        LabelLine(label = "单车耗时", count = minutes, tail = "分钟")
    }
}

@Composable
fun CaloriePerGramRow(
    onValueChange: ((Float, Float) -> Unit)? = null,
) {

    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        var grams by remember { mutableStateOf("100") }
        var kCalorie by remember { mutableStateOf("0") }

        fun notifyChange() {
            onValueChange?.invoke(
                grams.toFloatOrNull() ?: 0f,
                kCalorie.toFloatOrNull() ?: 0f,
            )
        }

        BasicTextField(
            value = grams,
            onValueChange = {
                grams = it
                notifyChange()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.weight(1f, false),
        )
        Text(text = "g热量:")
        BasicTextField(
            value = kCalorie,
            onValueChange = {
                kCalorie = it
                notifyChange()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.weight(1f),
        )
        Text(text = "千焦")
    }
}

@Composable
fun LabelLine(
    label: String,
    count: Float,
    tail: String,
    onValueChange: ((String) -> Unit)? = null,
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "$label:")
        if (onValueChange != null) {
            var value by remember { mutableStateOf("$count") }
            BasicTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                value = value,
                onValueChange = {
                    value = it
                    onValueChange(value)
                },

                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
//                .background(Color.White)
            )
        } else {
            Text(
                text = "$count",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
        }
        Text(text = tail)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthTheme {
        CalculateContent()
    }
}