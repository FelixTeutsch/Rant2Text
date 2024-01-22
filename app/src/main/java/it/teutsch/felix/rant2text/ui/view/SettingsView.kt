package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsView(
    closeSettingsIntent: () -> Unit
) {

    Scaffold(
        topBar = {
            SettingsTopBar(
                closeSettingsIntent = closeSettingsIntent
            )
        },
        content = { innerPadding ->
            SettingsContent(innerPadding)
        }
    )
}

@Composable
fun SettingsTopBar(
    closeSettingsIntent: () -> Unit
) {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(innerPadding: PaddingValues) {
    LazyColumn {
        stickyHeader {
            Text(text = "Usability")
        }

        item {
            ElevatedCard {
                Row {
                    Text(text = "Open RantChat on Create")
                    Switch(checked = true, onCheckedChange = {})
                }
            }
        }
    }
}