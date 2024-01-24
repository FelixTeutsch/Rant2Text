package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.data.model.TextTableModel
import it.teutsch.felix.rant2text.ui.model.RantChatModel
import it.teutsch.felix.rant2text.ui.voiceToText.VoicetoTextParser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RantChatView(
    rantChatModel: RantChatModel,
    rantId: Int,
    voiceToTextParser: VoicetoTextParser,
    settings: SettingsData,
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
    Log.d("personal", "color is ${state.value.angerLevel.angerColor}")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {
        topBarSection(
            title = state.value.rant.title,
            closeChatIntent,
            color = state.value.angerLevel.angerColor,
        )

        chatSection(
            modifier = Modifier.weight(0.9f),
            state.value.rant,
            rantChatModel,
            state.value.texts,
            settings
        )
        Log.d("rant", "rant is: ${state.value.rant}")
        messageOptions(
            modifier = Modifier.weight(0.12f),
            state.value.rant,
            rantChatModel,
            voiceToTextParser
        )

    }
}

@Composable
fun chatSection(
    modifier: Modifier,
    rant: RantTableModel,
    rantChatModel: RantChatModel,
    texts: List<TextTableModel>,
    settings: SettingsData
) {
    rantChatModel.getTextMsgs(rant.id)
    Log.d("msgsDb", "retrieved msgs are: ${texts.size}")

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = 0.8F)
            .padding(16.dp)
            .then(modifier),
        //so that the msgs dont stack upawards but instead get reversed
        reverseLayout = true,
    ) {
        items(texts.size) { index ->
            messageItem(texts[index], rantChatModel, settings, rant)
        }
    }

}

private val userChatBubble = RoundedCornerShape(10.dp, 10.dp, 0.dp, 10.dp)


@OptIn(
    ExperimentalAnimationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun messageItem(
    text: TextTableModel,
    rantChatModel: RantChatModel,
    settings: SettingsData,
    rant: RantTableModel
) {
    val sdf = SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault())
    val formattedDate = sdf.format(Date(text.date))
    var isTimeVisible by remember { mutableStateOf(settings.showTimeStampsForMessages) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(TextFieldValue(text.text)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { isTimeVisible = !isTimeVisible },
                onLongClick = {
                    isDialogOpen = true
                    editText = TextFieldValue(text.text)
                }),

        horizontalAlignment = Alignment.End
    ) {
        if (!text.text.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        //TEXT BOX COLOR IS HERE
                        MaterialTheme.colorScheme.primary,
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
                    text = text.text,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            AnimatedVisibility(
                visible = isTimeVisible,
                enter = slideInVertically(
                    initialOffsetY = { -40 },
                    animationSpec = tween(durationMillis = 400)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -20 },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.End),
                )
            }
        }
    }



    Spacer(modifier = Modifier.height(15.dp))

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = {
                isDialogOpen = false
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Edit Message",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            IconButton(
                                onClick = {
                                    rantChatModel.deleteTextMsg(rant, text)
                                    isDialogOpen = false
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "Delete Message",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {

                            var messageText by remember { mutableStateOf(TextFieldValue(text.text)) }
                            val oldText = text.text
                            OutlinedTextField(
                                value = messageText,
                                onValueChange = { messageText = it },
                                label = { Text("Message") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.White,
                                    focusedBorderColor = Color.Yellow,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )

                            ModalButtons(
                                confirmLabel = "Confirm",
                                confirmColor = MaterialTheme.colorScheme.primaryContainer,
                                onConfirmColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                cancelLabel = "Cancel",
                                onConfirm = {
                                    text.text = messageText.text
                                    rantChatModel.updateTextMsg(rant, oldText, text)
                                    isDialogOpen = false
                                },
                                onCancel = { isDialogOpen = false }
                            )
                        }
                    }
                }
            }
        )
    }
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
    Log.d("rantShow", "big rant rant is: $rant")
    val context = LocalContext.current

    var typedMsg by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue("")
        )
    }


    //ICON displays here!!
    val iconDisplay: Painter =
        if (typedMsg.text.isEmpty()) {
            if (!voiceRecState.value.isSpeaking) {
                painterResource(id = R.drawable.baseline_mic_24)
            } else {
                painterResource(id = R.drawable.baseline_stop_24)
            }
        } else {
            painterResource(R.drawable.baseline_send_24)
        }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = CardDefaults.cardColors(Color.Transparent)

//        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            OutlinedTextField(
                placeholder = { Text(text = "Type Message...", textAlign = TextAlign.Center) },
                value = typedMsg,
                onValueChange = { newText ->
                    typedMsg = newText
                },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .align(Alignment.CenterVertically),

                trailingIcon = {
                    Icon(
                        painter = iconDisplay,
                        contentDescription = "Send message button",
                        modifier = Modifier
                            .clickable {
                                if (typedMsg.text.isEmpty()) {
                                    //mic icon code
                                    if (voiceRecState.value.isSpeaking) {
                                        voiceToTextParser.stopListening()
                                    } else {
                                        voiceToTextParser.startListening()
                                    }
                                } else {
                                    rant.text = typedMsg.text
                                    saveTextMsg(rantChatModel, rant, typedMsg.text)
                                    rantChatModel.saveRantMsg(rant)
                                    typedMsg = TextFieldValue("")
                                }
                            }
                            .fillMaxHeight()
                            .fillMaxHeight()
                    )
                },
            )

            LaunchedEffect(voiceRecState.value.spokenText) {
                if (voiceRecState.value.spokenText.isNotEmpty()) {
                    rant.text = voiceRecState.value.spokenText
                    Log.d(
                        "msgsDb",
                        "rant id is: ${rant} when i send a voice msg using the mic icon: "
                    )

                    saveTextMsg(rantChatModel, rant, voiceRecState.value.spokenText)
                    rantChatModel.saveRantMsg(rant)
                }
            }
        }

    }
}

fun saveTextMsg(rantChatModel: RantChatModel, rant: RantTableModel, text: String) {

    Log.d("testRant", "rant is: $rant")
    rantChatModel.saveTextMsg(
        rant,
        TextTableModel(
            rantId = rant.id,
            text = text
        )
    )
}

@Composable
fun topBarSection(
    title: String,
    closeChatIntent: () -> Unit,
    color: Color,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        //TOPBAR color here
        colors = CardDefaults.cardColors(color.copy(alpha = 0.8F)),
        shape = RectangleShape,
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
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back to Rant list view Icon button",
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.dp)
                        .padding(10.dp)
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 15.dp),
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f)) // Push the icon to the right
            Box {
                IconButton(
                    onClick = { expanded = true },
                ) {
                    Crossfade(
                        targetState = expanded,
                        label = "expanding and shrinking the more options btn",
                        animationSpec = tween(durationMillis = 500)
                    ) { isExpanded ->
                        Icon(
                            // Switch between the two icons based on the expanded state
                            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(color = Color.DarkGray)
                ) {
                    DropdownMenuItem(
                        onClick = { "TODO: add the functionality" },
                        modifier = Modifier
                            .width(200.dp)
                    ) {
                        Text("Edit Rant", color = Color.White)
                    }
                }
            }
        }
    }
}
