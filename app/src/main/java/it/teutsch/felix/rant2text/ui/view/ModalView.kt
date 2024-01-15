package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import it.teutsch.felix.rant2text.ui.model.RantViewModel

// TODO: rework this function (itemName is hard to get from where it is activated)
@Composable
fun DeleteElementModal(
    itemName: String,
    showModal: Boolean = false,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    Dialog(
        onDismissRequest = {
            onCancel()
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
                    text = "Delete $itemName",
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
                            append("$itemName")
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
                    onConfirm = onConfirm,
                    onCancle = onCancel
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
    onCancle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                onCancle()
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = cancelLabel,
            )
        }

        Button(
            onClick = {
                onConfirm()
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        ) {
            Text(
                text = confirmLabel,
            )
        }
    }
}

@Composable
fun createRant(rantViewModel: RantViewModel, onConfirm: () -> Unit, onCancel: () -> Unit) {
    val state = rantViewModel.rantViewState.collectAsState()

    // if(state.value.dialog.equals(EDialog.CREATE_RANT) || state.value.dialog.equals(EDialog.EDIT_RANT))

}