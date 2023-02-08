package com.utopia.health

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = "热量计算器") }
                    )
                }) {
                    CalculateContent(modifier = Modifier.padding(it))
                }
            }
        }
    }
}

private const val TAG = "MainActivity"

@Composable
fun CalculateContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        var grams by remember { mutableStateOf(0f) }
        var kJoule by remember { mutableStateOf(0f) }
        var weight by remember { mutableStateOf(0f) }
        CalorieInputZone(
            onValueChange = { _grams, _kCalorie, _weight ->
                grams = _grams
                kJoule = _kCalorie
                weight = _weight
            },
        )
        Log.d(TAG, "CalculateContent: grams=$grams,kJoule=$kJoule,weight=$weight")

        val kCalorie = if (grams == 0f) 0f else (kJoule / grams * weight * 0.238846f)
        val minutes = kCalorie / 10f

        Log.d(TAG, "CalContent: kK=$kCalorie, minutes=$minutes")
        CardText(text = "千卡: $kCalorie")
        CardText(text = "单车耗时: $minutes 分钟")
    }
}

@Composable
fun CalorieInputZone(
    onValueChange: ((Float, Float, Float) -> Unit)? = null,
) {
    Column {
        var grams by remember { mutableStateOf("100") }
        var kCalorie by remember { mutableStateOf("") }
        var totalGrams by remember { mutableStateOf("") }
        fun notifyChange() {
            val callback = onValueChange ?: return
            val fGrams = grams.toFloatOrDefault(0f)
            val fKCalorie = kCalorie.toFloatOrDefault(0f)
            val fTotalGrams = totalGrams.toFloatOrDefault(0f)
            callback(fGrams, fKCalorie, fTotalGrams)
        }
        notifyChange()

        Row {
            CalorieInput(
                label = "每份重量:克",
                value = grams,
                onValueChange = {
                    grams = it
                    notifyChange()
                },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            CalorieInput(
                label = "每份热量:千焦",
                value = kCalorie,
                onValueChange = {
                    kCalorie = it
                    notifyChange()
                },
                modifier = Modifier.weight(1f),
            )
        }
        Row {
            CalorieInput(
                label = "总重量:克", value = totalGrams,
                onValueChange = {
                    totalGrams = it
                    notifyChange()
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun CalorieInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
    )
}

@Composable
fun CardText(text: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthTheme {
        CalculateContent()
    }
}