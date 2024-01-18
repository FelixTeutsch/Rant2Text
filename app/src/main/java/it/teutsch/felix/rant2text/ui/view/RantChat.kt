package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.model.RantChatModel
import it.teutsch.felix.rant2text.ui.voiceToText.VoicetoTextParser

@Composable
fun RantChatView(
    rantChatModel: RantChatModel,
    rantId: Int,
    voiceToTextParser: VoicetoTextParser,
    closeChatIntent: () -> Unit
) {
    val state = rantChatModel.rantChatState.collectAsState()
//
//    val ranty: RantTableModel = RantTableModel(
//        title = "life in general",
//        text = "i am about to yap about my life now for 400 words"
//    )
//    Log.d("intent", "$ranty")
//
//    rantChatModel.insertRant(ranty)

    try {
        rantChatModel.getRantById(rantId)
    } catch (e: Exception) {
        Log.d("err", "err is $e")

    }


    Log.d("dbwork", "state is ${state.value.rant.title}")

    Column(
        modifier = Modifier
            .fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        topBarSection(
            title = state.value.rant.title,
            closeChatIntent
        )
//        chatSection(state.value.text)
//        messageOptions()
        chatSection(messageText = state.value.rant.text, modifier = Modifier.weight(0.9f))
        messageOptions(
            modifier = Modifier.weight(0.1f),
            state.value.rant,
            rantChatModel,
            voiceToTextParser
        )

    }
}

@Composable
fun chatSection(messageText: String, modifier: Modifier) {

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = 0.8F)
            .padding(16.dp)
            .then(modifier),
        //so that the msgs dont stack upawards but instead get reversed
        reverseLayout = true
    ) {
        items(count = 1) {
            messageItem(messageText = messageText)
        }
    }

}

private val userChatBubble = RoundedCornerShape(10.dp, 10.dp, 0.dp, 10.dp)

@Composable
fun messageItem(messageText: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!messageText.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
//                        Color.White,
                        shape = userChatBubble
                    )
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = messageText,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Time soon",
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.Start),
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp))
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun messageOptions(
    modifier: Modifier,
    rant: RantTableModel,
    rantChatModel: RantChatModel,
    voiceToTextParser: VoicetoTextParser
) {
    val voiceRecState = voiceToTextParser.state.collectAsState()
//    val _voiceRecState = MutableStateFlow(VoiceToTextParserState())
//    val voiceRecState = _voiceRecState.collectAsState()

    val context = LocalContext.current

    var typedMsg by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
//        backgroundColor = Color.Red, // Set the background color directly on the Card
        colors = CardDefaults.cardColors(Color.Transparent)

//        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Red)
        ) {

            OutlinedTextField(
                placeholder = { Text(text = "Over write message") },
                value = typedMsg,
                onValueChange = { newText ->
                    typedMsg = newText
                },
                shape = RoundedCornerShape(25.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send message button",
                        modifier = Modifier
                            .clickable {
                                //                        Log.d("personal", "rant is: $rant")
                                rant.text = typedMsg.text
                                rantChatModel.saveRantMsg(rant)
                                typedMsg = TextFieldValue("")
                            }
                    )
                },
                modifier = Modifier
                    .weight(0.9F)
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(25.dp) // Set the same shape for the background
                    ),

                )

            Log.d(
                "personal",
                "spoken: ${voiceRecState.value.spokenText}, isspekaing: ${voiceRecState.value.isSpeaking}"
            )

            LaunchedEffect(voiceRecState.value.spokenText) {
                Log.d("personal", "test has been added: ${voiceRecState.value.spokenText}")
                rant.text = voiceRecState.value.spokenText
                rantChatModel.saveRantMsg(rant)
            }
            LaunchedEffect(voiceRecState.value.isSpeaking) {
                Log.d("personal", "ispseaking launched: ${voiceRecState.value.isSpeaking}")
            }

            IconButton(
                onClick = {
                    if (voiceRecState.value.isSpeaking) {
//                        Log.d("personal", "stop")

                        voiceToTextParser.stopListening()
                    } else {
//                        Log.d("personal", "listen")
                        voiceToTextParser.startListening()
//                        Log.d("personal", " after on${voiceRecState.value.isSpeaking}")
                    }
                    // typedMsg = TextFieldValue(voiceRecState.value.spokenText)
//                    Log.d("personal", "talk is: ${voiceRecState.value.spokenText}")
                },
                modifier = Modifier
                    .weight(0.1F)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.CenterVertically),

//                VerticalAlignmentLine = Alignment.CenterVertically
            ) {
                val micIcon: Painter = if (!voiceRecState.value.isSpeaking) {
                    painterResource(id = R.drawable.baseline_mic_24)
                } else {
                    painterResource(id = R.drawable.baseline_stop_24) // Set a different icon when the condition is false
                }
                Icon(
                    painter = micIcon,
                    contentDescription = "Send message button",
                    modifier = Modifier

                )
            }


            AnimatedContent(targetState = voiceRecState.value.isSpeaking, label = "") {

                if (it) {
                    Text(text = "hi")
                } else {
                    Text(text = "bye")
                }
            }
        }

    }
}

@Composable
fun topBarSection(title: String, closeChatIntent: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),


        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { closeChatIntent() },
                modifier = Modifier
                    .size(60.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Rant list view Icon button",
                    tint = Color.White,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(10.dp)
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 15.dp),
                text = '"' + title + '"',
                fontWeight = FontWeight.SemiBold,
            )
        }

    }
}

