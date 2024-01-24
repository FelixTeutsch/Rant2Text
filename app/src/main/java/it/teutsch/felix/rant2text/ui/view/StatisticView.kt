package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.model.RantViewModel

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticView(
    rantViewModel: RantViewModel,
    settings: SettingsData,
    openDrawer: () -> Unit,
    openRantChat: (id: Int) -> Unit
) {
    val mostCharsRant = remember { mutableStateOf<RantTableModel?>(null) }
    val leastCharsRant = remember { mutableStateOf<RantTableModel?>(null) }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getMostCharsRant().collect { rant ->
            if (rant != null) {
                mostCharsRant.value = rant
            }
        }
    }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getLeastCharsRant().collect { rant ->
            if (rant != null) {
                leastCharsRant.value = rant
            }
        }
    }


    Log.d(
        "mostChars",
        "mostCharsRant: ${mostCharsRant.value}, leastCharsRant: ${leastCharsRant.value}"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Statistics",
                        color = Color.LightGray
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF353535),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        },
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 25.dp)
        ) {
            LazyColumn {
                items(2) { // Replace 2 with the actual number of cards
                    CardWithImageAndText()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CardWithImageAndText() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
//                painter = painterResource(id = R.drawable.your_image),
                contentDescription = "Your Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Your Text",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
