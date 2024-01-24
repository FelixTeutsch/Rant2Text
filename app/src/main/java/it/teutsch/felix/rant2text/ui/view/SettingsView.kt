package it.teutsch.felix.rant2text.ui.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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