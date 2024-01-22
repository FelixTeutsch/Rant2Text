package it.teutsch.felix.rant2text

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import it.teutsch.felix.rant2text.data.RantDatabase
import it.teutsch.felix.rant2text.ui.model.RantViewModel
import it.teutsch.felix.rant2text.ui.theme.Rant2TextTheme
import it.teutsch.felix.rant2text.ui.view.RantListView

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(this, RantDatabase::class.java, "RantDatabase.db").build()
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
                    RantListView(rantViewModel) {
                        val intent = Intent(this, RantActivity::class.java).apply {
                            putExtra("rantId", it)
                        }
                        startActivity(intent)
                        rantViewModel.getRants()
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