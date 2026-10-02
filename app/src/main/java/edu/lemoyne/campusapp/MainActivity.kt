package edu.lemoyne.campusapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.lemoyne.campusapp.ui.theme.CampusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampusAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// --- Class 7: Step 1: a counter that remembers
@Composable
fun CounterDemo() {
    var count by remember { mutableStateOf(0) }

    Button(
        onClick = { count++ }
    ) {
        Text(text = "Tapped $count times")
    }
}

// --- Class 6: Step1: my own screen ---
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    // --- Class 7: Step 2: the list lives in state
    val trails = remember {
        mutableStateListOf("Green Lakes State Park", "Clark Reservation", "Highland Forest")
    }

    // --- Class 7: Step 3: what's typed lives in state ---
    var newTrail by remember { mutableStateOf("")}

    // --- Class 6: Step 3: a column, so things stack ---
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
//        CounterDemo()
        // --- Class 6: Step 4: real styling ---
        Text(
            text = "Hiking Log",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Trails I have walked this year",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Class 7: Step 3: the text field ---
        OutlinedTextField(
            value = newTrail,
            onValueChange = { newTrail = it },
            label = { Text("Trail name") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${newTrail.length} / 40",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Class 7: Step 4: the button changes the state ---
        Button(onClick = {
            trails.add(newTrail)
            newTrail = ""
        }) {
            Text("Add trail")
        }

        Button(onClick = {
            if (trails.isNotEmpty()) {
                trails.removeAt(trails.lastIndex)
            }
        }) {
            Text("Remove trail")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Class 7: Step 2: draw whatever is in the list ---
        Text(
            text = "${trails.size} trails",
            fontWeight = FontWeight.Bold
        )

        for (trail in trails) {
            Text(text = trail, fontSize = 18.sp)
        }
    }
}

// --- Class 6: Step2: preview ---
@Preview
@Composable
fun HomeScreenPreview() {
    CampusAppTheme {
        HomeScreen()
    }
}