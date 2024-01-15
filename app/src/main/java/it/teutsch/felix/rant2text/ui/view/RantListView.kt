package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.ui.model.RantViewModel

@Composable
fun RantListView(rantViewModel: RantViewModel, openRantChat: (id: Int) -> Unit) {
    val state = rantViewModel.rantViewState.collectAsState()

    // Load Rants from Database
    rantViewModel.getRants()

    Scaffold(
        topBar = {
            RantTopBar(
                title = "Rants",
                onRefreshClick = { rantViewModel.getRants() }
            )
        },
        content = { innerPadding ->

            if (state.value.rants.isEmpty())
                RantEmptyView(innerPadding) { rantViewModel.editRant() }
            else
                RantList(rantViewModel, innerPadding)
        },
        floatingActionButton = {
            RantFab(
                onClick = rantViewModel.editRant()
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
    )

    // Create & Delete Modals
    CreateRantModal(rantViewModel = rantViewModel, openRantChat)
    DeleteRantModal(rantViewModel = rantViewModel)
}

@Composable
fun RantEmptyView(innerPadding: PaddingValues, clickCreate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(innerPadding),
        contentAlignment = Alignment.Center

    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
            ),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        clickCreate()
                    },

                verticalArrangement = Arrangement.spacedBy(
                    alignment = Alignment.CenterVertically,
                    space = 16.dp,

                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "No Rants yet Logo",
                )
                Text(
                    text = "No rants yet. It's time for you to start complaining!",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    onClick = {
                        clickCreate()
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                ) {
                    Text(text = "Start Ranting")
                }
            }
        }
    }
}

@Composable
fun RantList(rantViewModel: RantViewModel, innerPadding: PaddingValues) {
    // TODO("Not yet implemented")
}

@Composable
fun RantFab(onClick: Unit) {
    FloatingActionButton(onClick = { onClick }) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Rant")
    }
}

@Composable
fun RantTopBar(title: String, onRefreshClick: () -> Unit) {
    // TODO("Not yet implemented")
}
