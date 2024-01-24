package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.model.RantViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticView(
    rantViewModel: RantViewModel,
    settings: SettingsData,
    openDrawer: () -> Unit,
    openRantChat: (id: Int) -> Unit
) {

    val selectedButtonIndex = remember { mutableIntStateOf(0) }

    val rantsArray = remember {
        arrayOf(
            arrayOf(
                mutableStateOf<RantTableModel?>(null),
                mutableStateOf<Boolean>(false)
            ), // mostCharsRant
            arrayOf(
                mutableStateOf<RantTableModel?>(null),
                mutableStateOf<Boolean>(false)
            )  // leastCharsRant
        )
    }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getMostCharsRant().collect { rant ->
            if (rant != null) {
                (rantsArray[0][0] as MutableState<RantTableModel?>).value =
                    rant // Update mostCharsRant
                (rantsArray[0][1] as MutableState<Boolean>).value = true // Update boolean
            }
        }
    }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getLeastCharsRant().collect { rant ->
            if (rant != null) {
                (rantsArray[1][0] as MutableState<RantTableModel?>).value =
                    rant // Update leastCharsRant
                (rantsArray[1][1] as MutableState<Boolean>).value = false // Update boolean
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rant Statistics",
                        color = Color.LightGray
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF353535),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedButtonIndex.value = 0
//                    onMonthRecapClicked()
                        }
                        .background(
                            color = if (selectedButtonIndex.value == 0) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Month Recap",
                        tint = Color.White
                    )
                    Text(
                        text = "Month Recap",
                        color = Color.White
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            selectedButtonIndex.value = 1
//                    onRecordsClicked()
                        }
                        .background(
                            color = if (selectedButtonIndex.value == 1) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image, // replace with your shape icon
                        contentDescription = "Records",
                        tint = Color.White
                    )
                    Text(
                        text = "Records",
                        color = Color.White
                    )
                }
            }
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 25.dp)
        ) {
            LazyColumn {
                items(2) { it ->
                    if (rantsArray[it][0].value != null) {
                        CardWithImageAndText(
                            rantsArray[it][0].value as RantTableModel?,
                            rantsArray[it][1].value as Boolean,
                            openRantChat
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun onMonthRecapClicked() {
}

@Composable
fun onRecordsClicked() {
}


@Composable
fun CardWithImageAndText(rant: RantTableModel?, longest: Boolean, openRantChat: (id: Int) -> Unit) {
    Log.d("CardWithImageAndText", "rant: $rant")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
//                .verticalScroll(rememberScrollState())
        ) {
            if (rant != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(rant.angerLevel.angerColor, CircleShape)
                            .padding(end = 8.dp),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    ) {
                        createHeaderText(longest)

                    }
                }
            }
            createImage(longest)
            if (rant != null) {
//                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                ) {

                    Text(
                        text = rant.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )

                    Text(
                        text = rant.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    createStatsText(rant, longest)

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { openRantChat(rant.id) },
                        modifier = Modifier

                            .height(IntrinsicSize.Min)
                            .align(Alignment.End),
                        shape = RoundedCornerShape(50) // This will make the button very rounded
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Read now!")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun createImage(longest: Boolean) {
    if (longest) {
        Image(
            painter = painterResource(id = R.drawable.empty_road_1447609),
            contentDescription = "Your Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.shortest_rant),
            contentDescription = "Your Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun createHeaderText(longest: Boolean) {
    if (longest) {
        Text(text = "Longest Rant")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "This is your longest rant")
    } else {
        Text(text = "Shortest Rant")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "This is your shortest rant")
    }
}

@Composable
fun createStatsText(rant: RantTableModel, longest: Boolean) {
    val fontSize = 18.sp
    if (longest) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("With ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        color = Color.Yellow
                    )
                ) {
                    append(rant.wordCount.toString())
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" words and ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        color = Color.Yellow
                    )
                ) {
                    append(rant.charCount.toString())
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" characters you managed to set your personal record for the")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 15.sp,
                        color = Color.Yellow
                    )
                ) {
                    append(" longest")
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" rant. Great Job!")
                }
            },
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("With only")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        color = Color.Yellow
                    )
                ) {
                    append(rant.wordCount.toString())
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" words and ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        color = Color.Yellow
                    )
                ) {
                    append(rant.charCount.toString())
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" characters you managed to set your personal record for the")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 15.sp,
                        color = Color.Yellow
                    )
                ) {
                    append(" shortest")
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(" rant. Rant more!")
                }
            },
//            modifier = Modifier.padding(8.dp)
        )
    }
}


