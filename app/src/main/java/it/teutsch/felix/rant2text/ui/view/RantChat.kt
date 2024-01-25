package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.data.model.RantTextTableModel
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
    val state by rantChatModel.rantChatState.collectAsState()

    try {
        rantChatModel.getRantById(rantId)
    } catch (e: Exception) {
        // Handle the exception if needed
    }

    Scaffold(
        topBar = {
            topBarSection(
                rantChatModel,
                title = state.rant.title,
                closeChatIntent,
                color = state.angerLevel.angerColor,
            )
        },
        content = {
            chatSection(
                state.rant,
                rantChatModel,
                state.texts,
                settings,
                it,
            )
        },
        bottomBar = {
            messageOptions(
                rant = state.rant,
                rantChatModel = rantChatModel,
                voiceToTextParser = voiceToTextParser
            )
        }
        // Not making use of bottom bar for style reasons
    )
    EditRantMessageModal(rantChatModel, settings)
    DeleteRantTextModal(rantChatModel)
    EditRantModalInner(rantChatModel, closeChat = closeChatIntent, settings = settings)
    DeleteRantModalInner(rantChatModel, closeChatIntent)
}

@Composable
fun chatSection(
    rant: RantListTableModel,
    rantChatModel: RantChatModel,
    texts: List<RantTextTableModel>,
    settings: SettingsData,
    innerPadding: PaddingValues,
) {
    rantChatModel.getRandMessages(rant.id)
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        reverseLayout = true,
    ) {
        items(texts) {
            messageItem(it, rantChatModel, settings, rant)
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
    text: RantTextTableModel,
    rantChatModel: RantChatModel,
    settings: SettingsData,
    rant: RantListTableModel
) {
    val sdf = SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault())
    val formattedDate = sdf.format(Date(text.date))
    var isTimeVisible by remember { mutableStateOf(settings.showTimeStampsForMessages) }
    var editText by remember { mutableStateOf(TextFieldValue(text.text)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { isTimeVisible = !isTimeVisible },
                onLongClick = {
                    if (settings.editRantMessages) {
                        rantChatModel.clickEditRant(text)
                        editText = TextFieldValue(text.text)
                    }
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
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun messageOptions(
    rant: RantListTableModel,
    rantChatModel: RantChatModel,
    voiceToTextParser: VoicetoTextParser
) {
    val state = rantChatModel.rantChatState.collectAsState()

    val voiceRecState = voiceToTextParser.state.collectAsState()

    var rantMessage by rememberSaveable { mutableStateOf(state.value.text) }


    // Define the icon to display based on the state of the text field
    val iconDisplay: ImageVector =
        if (rantMessage.isEmpty())
            if (!voiceRecState.value.isSpeaking)
                Icons.Rounded.Mic
            else
                Icons.Rounded.Stop
        else
            Icons.Rounded.Send

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                backgroundColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            placeholder = { Text(text = "Start Ranting...", textAlign = TextAlign.Center) },
            value = rantMessage,
            readOnly = voiceRecState.value.isSpeaking,
            onValueChange = {
                rantMessage = it
            },
            maxLines = 4,
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(25.dp)
                ),
        )
        // ICON TO SEND & Recordd
        Column(
            modifier = Modifier
                .padding(4.dp)
                .height(56.dp)
                .width(56.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val size by infiniteTransition.animateValue(
                initialValue = 56.dp,
                targetValue = 42.dp,
                Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "Animation for the mic icon"
            )
            val smallCircle by infiniteTransition.animateValue(
                initialValue = 28.dp,
                targetValue = 42.dp,
                Dp.VectorConverter,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "Animation for the mic icon"
            )
            Box(
                modifier = Modifier
                    .height(56.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (voiceRecState.value.isSpeaking) {
                    SimpleCircleShape2(
                        size = size,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                    )
                    SimpleCircleShape2(
                        size = smallCircle,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                    )
                    SimpleCircleShape2(
                        size = 28.dp,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                Column(
                    modifier = Modifier
                        .height(56.dp)
                        .width(56.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = iconDisplay,
                            contentDescription = "Send message button",
                            modifier = Modifier
                                .clickable {
                                    // Change of mic icon to send icon and back
                                    if (rantMessage.isEmpty())
                                        if (voiceRecState.value.isSpeaking)
                                            voiceToTextParser.stopListening()
                                        else
                                            voiceToTextParser.startListening()
                                    else {
                                        rant.text = rantMessage
                                        saveTextMsg(rantChatModel, rant, rantMessage)
                                        rantMessage = ""
                                    }
                                },
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(voiceRecState.value.spokenText) {
        if (voiceRecState.value.spokenText.isNotEmpty()) {
            rant.text = voiceRecState.value.spokenText
            saveTextMsg(rantChatModel, rant, voiceRecState.value.spokenText)
        }
    }
}

@Composable
fun SimpleCircleShape2(
    size: Dp,
    color: Color = Color.White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.LightGray.copy(alpha = 0.0f)
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    color
                )
                .border(borderWidth, borderColor)
        )
    }
}

fun saveTextMsg(rantChatModel: RantChatModel, rant: RantListTableModel, text: String) {
    rantChatModel.addRantMessage(
        rant,
        RantTextTableModel(
            rantId = rant.id,
            text = text
        )
    )
}

@Composable
fun topBarSection(
    rantChatModel: RantChatModel,
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
                        onClick = {
                            rantChatModel.clickUpdateRant()
                            expanded = false
                        },
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
