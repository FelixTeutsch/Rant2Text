package it.teutsch.felix.rant2text.ui.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.ui.model.RantViewModel

@Composable
fun StatisticView(
    rantViewModel: RantViewModel,
    settings: SettingsData,
    openDrawer: () -> Unit,
    openRantChat: (id: Int) -> Unit
) {
    Text(text = "StatisticView")
}