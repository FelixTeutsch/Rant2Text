package it.teutsch.felix.rant2text.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessibleForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.model.RantTableModel
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
                RantEmptyView(innerPadding) { rantViewModel.openEditModal() }
            else
                RantList(rantViewModel, innerPadding, openRantChat)
        },
        floatingActionButton = {
            RantFab { rantViewModel.openEditModal() }
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
fun RantList(
    rantViewModel: RantViewModel,
    innerPadding: PaddingValues,
    openRantChat: (id: Int) -> Unit
) {
    val state = rantViewModel.rantViewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(
            alignment = Alignment.CenterVertically,
            space = 16.dp,

            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.value.rants.forEach { rant ->
            RantCard(rant, rantViewModel, openRantChat)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RantCard(rant: RantTableModel, rantViewModel: RantViewModel, openRantChat: (id: Int) -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    // Do Something when swipe End To Start
                    rantViewModel.clickDeleteRant(rant)
                    false
                }

                else -> {
                    false
                }
            }
        }
    )

    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        SwipeToDismiss(state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            dismissThresholds = {
                FractionalThreshold(
                    0.05f
                )
            }, background = {
                val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

                val color by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error
                        else -> Color.LightGray
                    }, label = "Background Color"
                )
                val iconColor by animateColorAsState(
                    when (dismissState.targetValue) {
                        // DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.onPrimary
                        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.onError
                        else -> Color.LightGray
                    }, label = "Icon Color"
                )

                val alignment = when (direction) {
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                    // DismissDirection.StartToEnd -> Alignment.CenterStart
                    else -> Alignment.CenterStart
                }
                val icon = when (direction) {
                    // DismissDirection.StartToEnd -> Icons.Rounded.Edit
                    DismissDirection.EndToStart -> Icons.Rounded.Delete
                    else -> Icons.Rounded.AccessibleForward
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment
                ) {
                    Icon(
                        icon,
                        contentDescription = "Icon",
                        tint = iconColor
                    )
                }
            },
            dismissContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            openRantChat(rant.id)
                        }
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        alignment = Alignment.Start,
                        space = 16.dp
                    ),
                )
                {
                    // Colored Circle based on Anger Level
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(rant.angerLevel.angerColor, CircleShape)
                            .padding(end = 8.dp)
                            .fillMaxWidth(),
                    )
                    Column {
                        Text(
                            text = rant.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = rant.text,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            })
    }
}

@Composable
fun RantFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Icon(Icons.Rounded.Add, "Add Rant")
    }
}

@Composable
fun RantTopBar(title: String, onRefreshClick: () -> Unit) {
    // TODO("Not yet implemented")
}
