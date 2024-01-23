package it.teutsch.felix.rant2text

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import it.teutsch.felix.rant2text.data.RantDatabase
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.serializer.SettingsSerializer
import it.teutsch.felix.rant2text.ui.model.RantViewModel
import it.teutsch.felix.rant2text.ui.theme.Rant2TextTheme
import it.teutsch.felix.rant2text.ui.view.RantListView
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("app-settings.json", serializer = SettingsSerializer)

data class NavigationItem(
    val title: String,
    val icon: ImageVector? = null,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(this, RantDatabase::class.java, "RantDatabase.db")
            // TODO: remove once database changes and nothing works anymore
            //.fallbackToDestructiveMigration()
            .build()
    }

    private val rantViewModel by viewModels<RantViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RantViewModel(db.rantDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rant2TextTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val items = listOf(
                        NavigationItem(
                            title = "Rants",
                            icon = Icons.Rounded.Home,
                            badgeCount = 2
                        ),
                        NavigationItem(
                            title = "Statistics",
                            icon = Icons.Rounded.BarChart,
                        ),
                    )

                    val extra = listOf(
                        NavigationItem(
                            title = "Settings",
                            icon = Icons.Rounded.Settings,
                        ),
                        NavigationItem(
                            title = "About",
                            icon = Icons.Rounded.Info,
                        )
                    )

                    // DATA STORE
                    val settings = dataStore.data.collectAsState(initial = SettingsData()).value
                    // DrawerState
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(280.dp)
                                        .padding(16.dp)
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.logo),
                                            contentDescription = "No Rants yet Logo",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Text(
                                            text = "Rant2Text",
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier.padding(16.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Divider(
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    )

                                    items.forEachIndexed { index, item ->
                                        NavigationDrawerItem(
                                            label = { Text(text = item.title) },
                                            selected = index == selectedItemIndex,
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Text(text = item.badgeCount.toString())
                                                }
                                            },
                                            onClick = {
                                                selectedItemIndex = index
                                                scope.launch {
                                                    drawerState.close()
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = item.icon!!,
                                                    contentDescription = item.title
                                                )
                                            },
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))
                                    Divider(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    )

                                    extra.forEachIndexed { index, item ->
                                        NavigationDrawerItem(
                                            label = { Text(text = item.title) },
                                            selected = false,
                                            onClick = {
                                                scope.launch {
                                                    drawerState.close()
                                                }
                                                if (index == 0) {
                                                    startActivity(
                                                        Intent(
                                                            this@MainActivity,
                                                            SettingsActivity::class.java
                                                        )
                                                    )
                                                } else if (index == 1) {
                                                    // TODO(Create about Activity)
//                                                    startActivity(
//                                                        Intent(
//                                                            this@MainActivity,
//                                                            AboutActivity::class.java
//                                                        )
//                                                    )
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = item.icon!!,
                                                    contentDescription = item.title
                                                )
                                            },
                                        )
                                    }

                                }
                            }
                        },
                        drawerState = drawerState,
                    ) {
                        if (selectedItemIndex == 0) {
                            RantListView(
                                rantViewModel,
                                settings = settings,
                                openDrawer = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                openRantChat = {
                                    val intent = Intent(this, RantActivity::class.java).apply {
                                        putExtra("rantId", it)
                                    }
                                    startActivity(intent)
                                    rantViewModel.getRants()
                                },
                            )
                        } else if (selectedItemIndex == 1)
                            Text(text = "Statistics")

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rantViewModel.getRants()
    }
}