package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    settings: SettingsData,
    scope: CoroutineScope,
    dataStore: DataStore<SettingsData>,
    closeSettingsIntent: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Back to Overview",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                closeSettingsIntent()
                            }
                    )
                },
            )
        },
        content = { innerPadding ->
            SettingsContent(innerPadding, settings, scope, dataStore)
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(
    innerPadding: PaddingValues,
    settings: SettingsData,
    scope: CoroutineScope,
    dataStore: DataStore<SettingsData>
) {

    LazyColumn(
        modifier = Modifier.padding(innerPadding),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            SettingsGroup(title = "Rants") {
                SettingsItemSwitch(
                    title = "Open Rant on Create",
                    description = "Immediately open Rant Chat when creating a new Rant",
                    checked = settings.openRantOnCreate,
                    onSwitchChange = {
                        scope.launch {
                            dataStore.updateData { currentSettings ->
                                currentSettings.copy(openRantOnCreate = it)
                            }
                        }
                    }
                )
                SettingsItemSwitch(
                    title = "Open Rant on Edit",
                    description = "Open Rant Chat when editing it",
                    checked = settings.openRantOnEdit,
                    onSwitchChange = {
                        scope.launch {
                            dataStore.updateData { currentSettings ->
                                currentSettings.copy(openRantOnEdit = it)
                            }
                        }
                    }
                )
                SettingsItemSlider(
                    title = "Default Anger Level",
                    description = "Default Anger Level for new Rants",
                    settings = settings,
                    scope = scope,
                    dataStore = dataStore
                )

                SettingsItemSwitch(
                    title = "Confirm before deleting",
                    description = "Open Confirm Modal before deleting a Rant",
                    checked = settings.confirmBeforeDelete,
                    onSwitchChange = {
                        scope.launch {
                            dataStore.updateData { currentSettings ->
                                currentSettings.copy(confirmBeforeDelete = it)
                            }
                        }
                    }
                )
            }

            SettingsGroup(title = "Rant Chat") {
                SettingsItemSwitch(
                    title = "Show timestamps for messages",
                    description = "Display all timestamps in a chat by default (without clicking on it)",
                    checked = settings.showTimeStampsForMessages,
                    onSwitchChange = {
                        scope.launch {
                            dataStore.updateData { currentSettings ->
                                currentSettings.copy(showTimeStampsForMessages = it)
                            }
                        }
                    }
                )
            }
            SettingsGroup(title = "Extra") {
                SettingsItemFirebaseID(context = LocalContext.current)

                SettingsItemTextCopy(
                    title = "App Version",
                    textToCopy = LocalContext.current.packageManager.getPackageInfo(
                        LocalContext.current.packageName,
                        0
                    ).versionName,
                    context = LocalContext.current
                )
            }
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
        }

        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
        ) {
            content()
        }
    }
}

@Composable
fun SettingsItemSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onSwitchChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked, onCheckedChange = onSwitchChange,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SettingsItemSlider(
    title: String,
    description: String,
    settings: SettingsData,
    scope: CoroutineScope,
    dataStore: DataStore<SettingsData>,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Slider(
            value = settings.defaultAngerLevel.angerLevel.toFloat(),
            onValueChange = {
                scope.launch {
                    dataStore.updateData { currentSettings ->
                        currentSettings.copy(defaultAngerLevel = EAngerLevel.fromInt(it.toInt()))
                    }
                }
            },
            valueRange = 0f..5f,
            steps = EAngerLevel.values().size - 2,
            colors = SliderDefaults.colors(
                thumbColor = settings.defaultAngerLevel.angerColor,
                activeTrackColor = settings.defaultAngerLevel.angerColor,
            )
        )
        Text(
            text = settings.defaultAngerLevel.angerName,
            color = settings.defaultAngerLevel.angerColor
        )
    }
}

@Composable
fun SettingsItemTextCopy(title: String, textToCopy: String, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(
                text = textToCopy, // Display Firebase ID
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Rounded.ContentCopy,
            contentDescription = "Copy $title",
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    // Copy Firebase ID to clipboard
                    val clipboard =
                        context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip =
                        android.content.ClipData.newPlainText(title, textToCopy)
                    clipboard.setPrimaryClip(clip)
                }
        )
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SettingsItemFirebaseID(context: Context) {
    // Create a coroutine scope
    val coroutineScope = rememberCoroutineScope()

    // Create a MutableState to hold the Firebase ID
    val firebaseIdState = remember { mutableStateOf("") }

    // Launch a coroutine to retrieve the Firebase ID
    coroutineScope.launch {
        try {
            // Use the FCM library to get the token
            val firebaseId = Firebase.messaging.token.await()
            // Update the state with the obtained Firebase ID
            firebaseIdState.value = firebaseId
        } catch (e: Exception) {
            // Handle exceptions, if any
            e.printStackTrace()
        }
    }

    SettingsItemTextCopy(
        title = "Firebase ID",
        textToCopy = firebaseIdState.value,
        context = context
    )
}
