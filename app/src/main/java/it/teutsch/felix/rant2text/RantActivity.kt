package it.teutsch.felix.rant2text

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import it.teutsch.felix.rant2text.data.RantDatabase
import it.teutsch.felix.rant2text.ui.model.RantChatModel
import it.teutsch.felix.rant2text.ui.theme.Rant2TextTheme
import it.teutsch.felix.rant2text.ui.view.RantChatView
import it.teutsch.felix.rant2text.ui.voiceToText.VoicetoTextParser

class RantActivity : ComponentActivity() {

    private val voiceToTextParser by lazy {
        VoicetoTextParser(application)
    }

    private val db by lazy {
        Room.databaseBuilder(this, RantDatabase::class.java, "RantDatabase.db").build()
    }

    private val rantChatModel by viewModels<RantChatModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RantChatModel(db.rantDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var canRecord by remember {
                mutableStateOf(false)
            }
            val recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isgranted ->
                    canRecord = isgranted
                })

            LaunchedEffect(key1 = recordAudioLauncher) {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }


            Rant2TextTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val rantId = intent.getIntExtra("rantId", 1)
//                    Text(text = "RantActivity $rantId")
                    RantChatView(rantChatModel, rantId, voiceToTextParser) {
                        db.close()
                        finish()
                    }
                }
            }
        }
    }
}