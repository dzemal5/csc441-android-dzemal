package edu.lemoyne.campusapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.lemoyne.campusapp.ui.theme.CampusAppTheme

// --- Class 8 · Step 2: one rule book for trail names ---
const val MAX_NAME_LENGTH = 40

fun validateTrailName(input: String, existing: List<String>): String? {
    val name = input.trim()
    return when {
        name.isEmpty() -> "Enter a trail name"
        name.length > MAX_NAME_LENGTH -> "Keep it to $MAX_NAME_LENGTH characters or fewer"
        existing.any { it.equals(name, ignoreCase = true) } -> "\"$name\" is already on the list"
        else -> null
    }
}

// --- Class 7 · Step 1: a counter that remembers ---
@Composable
fun CounterDemo() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = {
        count++
        println("count is now $count")
    }) {
        Text("Tapped $count times")
    }
}

// --- Class 9 · Step 2: one owner for the data ---
@Composable
fun CampusAppScreen(modifier: Modifier = Modifier) {
    // --- Class 7 · Step 2: the list lives in state ---
    val trails = remember {
        mutableStateListOf("Green Lakes State Park", "Clark Reservation", "Highland Forest")
    }

    // --- Class 9 · Step 4: which screen is showing is just state ---
    var currentScreen by rememberSaveable { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(
            trails = trails,
            onAddTrail = { trails.add(it) },
            onSeeAll = { currentScreen = "list" },
            modifier = modifier
        )
        "list" -> ListScreen(
            trails = trails,
            onBack = { currentScreen = "home" },
            // --- Class 10 · Step 4: only the owner changes the list ---
            onRemove = { trails.remove(it) },
            modifier = modifier
        )
    }
}

// --- Class 9 · Step 2: HomeScreen gets its data from outside ---
@Composable
fun HomeScreen(
    trails: List<String>,
    onAddTrail: (String) -> Unit,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Class 7 · Step 3: what's typed lives in state ---
    var newTrail by remember { mutableStateOf("") }

    // --- Class 8 · Step 3: the error message lives in state too ---
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
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

        // --- Class 7 · Step 3: the text field ---
        OutlinedTextField(
            value = newTrail,
            // --- Class 8 · Step 4: the field itself pushes back ---
            onValueChange = {
                newTrail = it.take(MAX_NAME_LENGTH)
                errorMessage = null
            },
            label = { Text("Trail name") },
            singleLine = true,
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        // --- Class 8 · Step 3: show the problem ---
        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Class 7 · Step 4: the button changes the state ---
        Button(
            onClick = {
                // --- Class 8 · Step 3: check before you add ---
                val problem = validateTrailName(newTrail, trails)
                if (problem == null) {
                    // --- Class 9 · Step 2: ask the owner to add it ---
                    onAddTrail(newTrail.trim())
                    newTrail = ""
                    errorMessage = null
                } else {
                    errorMessage = problem
                }
            },
            // --- Class 8 · Step 5: the sign on the door, not the lock ---
            enabled = newTrail.isNotBlank()
        ) {
            Text("Add trail")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Class 7 · Step 2: draw whatever is in the list ---
        Text(
            text = "${trails.size} trails",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Class 9 · Step 5: a way to the second screen ---
        Button(onClick = onSeeAll) {
            Text("See all trails")
        }
    }
}

// --- Class 9 · Step 3: the second screen ---
@Composable
fun ListScreen(
    trails: List<String>,
    onBack: () -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Class 9 · Step 6: the phone's back button goes home too ---
    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text(
            text = "All trails",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // --- Lab 9 · Task 1: count on the list screen ---
        Text(
            text = "${trails.size} trails",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Class 10 · Step 5: the empty case ---
        if (trails.isEmpty()) {
            Text(
                text = "No trails yet. Add one on the home screen.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // --- Class 10 · Step 2: a list that scrolls ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(trails) { trail ->
                    TrailRow(
                        name = trail,
                        onRemove = { onRemove(trail) }
                    )
                }
            }
        }
    }
}

// --- Class 10 · Step 3: one row, as its own Composable ---
@Composable
fun TrailRow(
    name: String,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            // --- Class 10 · Step 4: a remove button on every row ---
            TextButton(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}

// --- Class 9 · Step 2: previews need sample data now ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CampusAppTheme {
        HomeScreen(
            trails = listOf("Green Lakes State Park", "Clark Reservation"),
            onAddTrail = {},
            onSeeAll = {}
        )
    }
}

// --- Class 9 · Step 7: preview the list screen ---
@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    CampusAppTheme {
        ListScreen(
            trails = listOf("Green Lakes State Park", "Clark Reservation", "Highland Forest"),
            onBack = {},
            onRemove = {}
        )
    }
}

// --- Class 10 · Step 5: preview the empty case too ---
@Preview(showBackground = true)
@Composable
fun ListScreenEmptyPreview() {
    CampusAppTheme {
        ListScreen(
            trails = emptyList(),
            onBack = {},
            onRemove = {}
        )
    }
}
