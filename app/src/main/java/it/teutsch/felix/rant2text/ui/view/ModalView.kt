package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.data.model.RantTextTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog
import it.teutsch.felix.rant2text.ui.model.RantChatModel
import it.teutsch.felix.rant2text.ui.model.RantViewModel
import kotlinx.coroutines.launch

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
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween

                        ) {
                            Text(
                                text = "Delete ${state.value.targetRant.title}",
                                style = MaterialTheme.typography.headlineMedium,
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


                            ModalButtons(
                                confirmLabel = "Delete",
                                confirmColor = MaterialTheme.colorScheme.error,
                                onConfirmColor = MaterialTheme.colorScheme.onError,
                                cancelLabel = "Cancel",
                                onConfirm = { rantViewModel.deleteRant(state.value.targetRant) },
                                onCancel = { rantViewModel.dismissDialog() }
                            )
                        }
                    }
                }
            }
        )

}

@Composable
fun ModalButtons(
    confirmLabel: String,
    confirmColor: Color,
    onConfirmColor: Color,
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
            Text(text = cancelLabel, color = MaterialTheme.colorScheme.onSurface)
        }
        Button(
            onClick = { onConfirm() },
            colors = ButtonDefaults.buttonColors(
                contentColor = onConfirmColor,
                containerColor = confirmColor
            ),

            ) {
            Text(text = confirmLabel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRantModal(
    rantViewModel: RantViewModel,
    openRantChat: (Int) -> Unit,
    settings: SettingsData? = null
) {
    val state = rantViewModel.rantViewState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (state.value.dialog == EDialog.CREATE_RANT || state.value.dialog == EDialog.EDIT_RANT) {
        val isEditMode = state.value.dialog == EDialog.EDIT_RANT
        val title = if (isEditMode) "Edit Rant" else "Create Rant"
        val rant = state.value.targetRant

        // Composable content for your modal
        Dialog(
            onDismissRequest = {
                rantViewModel.dismissDialog()
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Title
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // Delete button in case of Edit mode
                            if (isEditMode) {
                                IconButton(
                                    onClick = {
                                        if (settings == null || settings.confirmBeforeDeleteRantList) rantViewModel.clickDeleteRant(
                                            rant
                                        ) else rantViewModel.deleteRant(rant)
                                    },
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete Rant",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        var rantTitle by rememberSaveable { mutableStateOf(rant.title) }
                        OutlinedTextField(
                            value = rantTitle,
                            onValueChange = { rantTitle = it },
                            label = { Text("Rant Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!isEditMode && settings != null) {
                            rant.angerLevel =
                                EAngerLevel.fromInt(settings.defaultAngerLevel.angerLevel)
                        }

                        var rantLevel by rememberSaveable { mutableStateOf(rant.angerLevel) }
                        var rantValue by rememberSaveable { mutableFloatStateOf(rant.angerLevel.angerLevel.toFloat()) }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("How angry are you?", style = MaterialTheme.typography.labelMedium)
                            Slider(
                                value = rantValue,
                                onValueChange = {
                                    rantLevel = EAngerLevel.fromInt(it.toInt())
                                    rantValue = it
                                },
                                valueRange = 0f..5f,
                                steps = EAngerLevel.values().size - 2,
                                colors = SliderDefaults.colors(
                                    thumbColor = rantLevel.angerColor,
                                    activeTrackColor = rantLevel.angerColor,
                                )
                            )
                            Text(text = rantLevel.angerName, color = rantLevel.angerColor)
                        }

                        // Buttons (Cancel and Confirm)
                        ModalButtons(
                            confirmLabel = "Confirm",
                            confirmColor = MaterialTheme.colorScheme.primaryContainer,
                            onConfirmColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            cancelLabel = "Cancel",
                            onConfirm = {
                                if (rantTitle.isEmpty()) return@ModalButtons
                                rantViewModel.dismissDialog()
                                if (isEditMode) {
                                    coroutineScope.launch {
                                        rantViewModel.updateRant(
                                            RantListTableModel(
                                                title = rantTitle,
                                                text = rant.text,
                                                angerLevel = EAngerLevel.fromInt(rantLevel.angerLevel),
                                                id = rant.id
                                            )
                                        )

                                        if (settings?.openRantOnEdit == true)
                                            openRantChat(rant.id)
                                    }
                                } else {
                                    coroutineScope.launch {
                                        val newRantId = rantViewModel.saveRant(
                                            RantListTableModel(
                                                title = rantTitle,
                                                text = rant.text,
                                                angerLevel = rantLevel,
                                            )
                                        )
                                        if (settings?.openRantOnCreate == true)
                                            openRantChat(newRantId)
                                    }
                                }
                            },
                            onCancel = { rantViewModel.dismissDialog() }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun EditRantModalInner(
    rantChatModel: RantChatModel,
    closeChat: () -> Unit,
    settings: SettingsData? = null
) {
    val state = rantChatModel.rantChatState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (state.value.dialog == EDialog.EDIT_RANT) {
        val rant = state.value.rant

        // Composable content for your modal
        Dialog(
            onDismissRequest = {
                rantChatModel.dismissDialog()
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Title
                            Text(
                                text = "Edit Rant",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // Delete button in case of Edit mode
                            IconButton(
                                onClick = {
                                    if (settings == null || settings.confirmBeforeDeleteRantList)
                                        rantChatModel.clickDeleteRant()
                                    else {
                                        rantChatModel.deleteRant(rant)
                                        closeChat()
                                    }
                                },
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "Delete Rant",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        var rantTitle by rememberSaveable { mutableStateOf(rant.title) }
                        OutlinedTextField(
                            value = rantTitle,
                            onValueChange = { rantTitle = it },
                            label = { Text("Rant Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        var rantLevel by rememberSaveable { mutableStateOf(rant.angerLevel) }
                        var rantValue by rememberSaveable { mutableFloatStateOf(rant.angerLevel.angerLevel.toFloat()) }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("How angry are you?", style = MaterialTheme.typography.labelMedium)
                            Slider(
                                value = rantValue,
                                onValueChange = {
                                    rantLevel = EAngerLevel.fromInt(it.toInt())
                                    rantValue = it
                                },
                                valueRange = 0f..5f,
                                steps = EAngerLevel.values().size - 2,
                                colors = SliderDefaults.colors(
                                    thumbColor = rantLevel.angerColor,
                                    activeTrackColor = rantLevel.angerColor,
                                )
                            )
                            Text(text = rantLevel.angerName, color = rantLevel.angerColor)
                        }

                        // Buttons (Cancel and Confirm)
                        ModalButtons(
                            confirmLabel = "Confirm",
                            confirmColor = MaterialTheme.colorScheme.primaryContainer,
                            onConfirmColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            cancelLabel = "Cancel",
                            onConfirm = {
                                if (rantTitle.isEmpty()) return@ModalButtons
                                rantChatModel.dismissDialog()
                                rantChatModel.updateRant(
                                    RantListTableModel(
                                        title = rantTitle,
                                        text = rant.text,
                                        angerLevel = EAngerLevel.fromInt(rantLevel.angerLevel),
                                        id = rant.id
                                    )
                                )


                            },
                            onCancel = { rantChatModel.dismissDialog() }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun DeleteRantModalInner(
    rantChatModel: RantChatModel
) {
    val state = rantChatModel.rantChatState.collectAsState()
    if (state.value.dialog == EDialog.DELETE_RANT)
        Dialog(
            onDismissRequest = {
                rantChatModel.dismissDialog()
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween

                        ) {
                            Text(
                                text = "Delete ${state.value.rant.title}",
                                style = MaterialTheme.typography.headlineMedium,
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
                                        append(state.value.rant.title)
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


                            ModalButtons(
                                confirmLabel = "Delete",
                                confirmColor = MaterialTheme.colorScheme.error,
                                onConfirmColor = MaterialTheme.colorScheme.onError,
                                cancelLabel = "Cancel",
                                onConfirm = { rantChatModel.deleteRant(rant = state.value.rant) },
                                onCancel = { rantChatModel.dismissDialog() }
                            )
                        }
                    }
                }
            }
        )

}

@Composable
fun DeleteRantTextModal(rantChatModel: RantChatModel) {
// TODO: Implement Delete
    val state = rantChatModel.rantChatState.collectAsState()
    if (state.value.dialog == EDialog.DELETE_TEXT)
        Dialog(
            onDismissRequest = {
                rantChatModel.dismissDialog()
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween

                        ) {
                            Text(
                                text = "Delete Message",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                            )

                            Text(
                                text = "Are you sure you want to delete this rant message?",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = state.value.targetMessage.text,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "This action cannot be undone!",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )


                            ModalButtons(
                                confirmLabel = "Delete",
                                confirmColor = MaterialTheme.colorScheme.error,
                                onConfirmColor = MaterialTheme.colorScheme.onError,
                                cancelLabel = "Cancel",
                                onConfirm = {
                                    rantChatModel.deleteRantMessage(
                                        text = state.value.targetMessage,
                                        rant = state.value.rant
                                    )
                                },
                                onCancel = { rantChatModel.dismissDialog() }
                            )
                        }
                    }
                }
            }
        )


    //rantChatModel.deleteRantMessage(rant, text)
}

@Composable
fun EditRantMessageModal(rantChatModel: RantChatModel) {
    var state = rantChatModel.rantChatState.collectAsState()

    if (state.value.dialog == EDialog.EDIT_TEXT) {
        var targetMessage = state.value.targetMessage
        var messageText by rememberSaveable { mutableStateOf(targetMessage.text) }

        Dialog(
            onDismissRequest = {
                rantChatModel.dismissDialog()
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Edit Message",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            IconButton(
                                onClick = {
                                    rantChatModel.clickDeleteRantMessage()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "Delete Message",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {

                            OutlinedTextField(
                                value = messageText,
                                onValueChange = { messageText = it },
                                label = { Text("Rant Message") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ModalButtons(
                                confirmLabel = "Confirm",
                                confirmColor = MaterialTheme.colorScheme.primaryContainer,
                                onConfirmColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                cancelLabel = "Cancel",
                                onConfirm = {
                                    if (messageText.isBlank())
                                        return@ModalButtons
                                    rantChatModel.updateRantMessage(
                                        RantTextTableModel(
                                            date = targetMessage.date,
                                            text = messageText,
                                            rantId = targetMessage.rantId,
                                            id = targetMessage.id
                                        ),
                                        targetMessage.text
                                    )
                                },
                                onCancel = { rantChatModel.dismissDialog() }
                            )
                        }
                    }
                }
            }
        )
    }
}
