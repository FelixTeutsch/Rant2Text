package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog
import it.teutsch.felix.rant2text.ui.model.RantViewModel

// TODO: rework this function (itemName is hard to get from where it is activated)
@Composable
fun DeleteRantModal(
    rantViewModel: RantViewModel
) {
    val state = rantViewModel.rantViewState.collectAsState()
    if (state.value.dialog == EDialog.DELETE_RANT)
        Dialog(
            onDismissRequest = {
                rantViewModel.dismissDialog()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        text = "Delete ${state.value.targetRant.title}",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("Are you sure you want to delete ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            ) {
                                append(state.value.targetRant.title)
                            }

                            append(
                                "?"
                            )
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        text = "This action cannot be undone!",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )


                    modalButtons(
                        confirmLabel = "Delete",
                        confirmColor = MaterialTheme.colorScheme.error,
                        cancelLabel = "Cancel",
                        onConfirm = { rantViewModel.deleteRant(state.value.targetRant) },
                        onCancel = { rantViewModel.dismissDialog() }
                    )
                }
            }
        }

}

@Composable
fun modalButtons(
    confirmLabel: String,
    confirmColor: Color,
    cancelLabel: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { onCancel() }) {
            Text(text = cancelLabel)
        }
        TextButton(onClick = { onConfirm() }) {
            Text(text = confirmLabel, color = confirmColor)
        }
    }
}

@Composable
fun CreateRantModal(
    rantViewModel: RantViewModel,
    openRantChat: (Int) -> Unit
) {
    val state = rantViewModel.rantViewState.collectAsState()

    if (state.value.dialog == EDialog.CREATE_RANT || state.value.dialog == EDialog.EDIT_RANT) {
        val isEditMode = state.value.dialog == EDialog.EDIT_RANT
        val title = if (isEditMode) "Edit Rant" else "Create Rant"
        val rant =
            state.value.targetRant // Assuming you have a property for the edited rant in your view state

        // Composable content for your modal
        Dialog(
            onDismissRequest = { rantViewModel.dismissDialog() }
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Rant Title Text Input
                var rantTitle by rememberSaveable { mutableStateOf(rant.title) }
                TextField(
                    value = rantTitle,
                    onValueChange = { rantTitle = it },
                    label = { Text("Rant Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Slider for Rant Level
                var rantLevel by rememberSaveable { mutableStateOf(rant.angerLevel.angerLevel.toFloat()) }
                Slider(
                    value = rantLevel,
                    onValueChange = { rantLevel = it },
                    valueRange = 0f..5f,
                    steps = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Delete button in case of Edit mode
                if (isEditMode) {
                    IconButton(
                        onClick = { rantViewModel.clickDeleteRant(rant) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete Rant",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Buttons (Cancel and Confirm)
                modalButtons(
                    confirmLabel = "Confirm",
                    confirmColor = Color.Green,
                    cancelLabel = "Cancel",
                    onConfirm = {
                        rantViewModel.dismissDialog()
                        if (isEditMode) {
                            rantViewModel.updateRant(
                                RantTableModel(
                                    title = rantTitle,
                                    text = rant.text,
                                    angerLevel = EAngerLevel.fromInt(rantLevel.toInt()),
                                    id = rant.id
                                )
                            )
                        } else {
                            var newRantId = rantViewModel.saveRant(
                                RantTableModel(
                                    title = rantTitle,
                                    text = rant.text,
                                    angerLevel = EAngerLevel.fromInt(rantLevel.toInt())
                                )
                            )
                            openRantChat(newRantId)
                        }
                    },
                    onCancel = { rantViewModel.dismissDialog() }
                )
            }
        }
    }
}
