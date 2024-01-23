package it.teutsch.felix.rant2text

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.dataStore
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.serializer.SettingsSerializer
import it.teutsch.felix.rant2text.ui.theme.Rant2TextTheme
import it.teutsch.felix.rant2text.ui.view.SettingsView


class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rant2TextTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val settings = dataStore.data.collectAsState(initial = SettingsData()).value
                    val scope = rememberCoroutineScope()

                    SettingsView(settings, scope, dataStore) {
                        finish()
                    }
                }
            }
        }
    }
}