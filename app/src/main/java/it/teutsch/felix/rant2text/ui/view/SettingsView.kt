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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
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
            SettingsGroup(title = "General") {
                SettingsItem(
                    title = "Open Rant on Edit",
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
                SettingsItem(
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
                SettingsItem(
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
fun SettingsItem(
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