package it.teutsch.felix.rant2text.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.ui.model.RantViewModel
import it.teutsch.felix.rant2text.ui.state.Statistics_screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


sealed class Screen(val route: String) {
    object Week_Summary : Screen("week_Summary")
    object Month_Recap : Screen("month_Recap")
    object Records : Screen("records")
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun StatisticView(
    rantViewModel: RantViewModel,
    settings: SettingsData,
    openDrawer: () -> Unit,
    openRantChat: (id: Int) -> Unit
) {
    val _Statistics_screens = MutableStateFlow(Statistics_screens())

    val navController = rememberNavController()

    val selectedButtonIndex = remember { mutableIntStateOf(1) }

    val rantsArray = remember {
        arrayOf(
            arrayOf(
                mutableStateOf<RantListTableModel?>(null),
                mutableStateOf<Boolean>(false)
            ), // mostCharsRant
            arrayOf(
                mutableStateOf<RantListTableModel?>(null),
                mutableStateOf<Boolean>(false)
            )  // leastCharsRant
        )
    }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getMostCharsRant().collect { rant ->
            if (rant != null) {
                (rantsArray[0][0] as MutableState<RantListTableModel?>).value =
                    rant // Update mostCharsRant
                (rantsArray[0][1] as MutableState<Boolean>).value = true // Update boolean
            }
        }
    }

    LaunchedEffect(rantViewModel) {
        rantViewModel.getLeastCharsRant().collect { rant ->
            if (rant != null) {
                (rantsArray[1][0] as MutableState<RantListTableModel?>).value =
                    rant // Update leastCharsRant
                (rantsArray[1][1] as MutableState<Boolean>).value = false // Update boolean
            }
        }
    }
    val pagerState = rememberPagerState(initialPage = 2)

    val pageNames = listOf("Month Recap", "Records")

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


            if (settings.scrollNavigation) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
//                            .background(Color.LightGray)
                            .align(Alignment.CenterVertically),
//                            .fillMaxHeight(),
//                            .width(100.dp),
                        activeColor = Color(0xFF755C00),
                        inactiveColor = Color.Gray.copy(0.5f),
                        indicatorWidth = 12.dp,
                        indicatorHeight = 12.dp,
                        spacing = 5.dp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                selectedButtonIndex.value = 0
                                navController.navigate(Screen.Month_Recap.route)
                            }
                            .background(
                                color = if (selectedButtonIndex.value == 0) Color(0x80755C00) else Color.Transparent,
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
                                navController.navigate(Screen.Records.route)

//                    onRecordsClicked()
                            }
                            .indication(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                            .background(
                                color = if (selectedButtonIndex.value == 1) Color(0x80755C00) else Color.Transparent,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(10.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24),
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

        }

    ) { pads ->


        if (settings.scrollNavigation) {
            Column {
                HorizontalPager(
                    state = pagerState,
                    count = 2,
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) { page ->
                    when (page) {
                        0 -> {
                            _Statistics_screens.update { it.copy(selectedScreen = Screen.Month_Recap) }
                            monthlyRecapView()
                        }

                        1 -> {
                            _Statistics_screens.update { it.copy(selectedScreen = Screen.Records) }
                            recordsView(rantsArray, openRantChat, pads)
                        }
                    }
                }
            }
        } else {

            NavHost(
                navController = navController,
                modifier = Modifier
                    .padding(top = 20.dp),

                startDestination = Screen.Records.route
            ) {
                composable(Screen.Month_Recap.route) {
                    _Statistics_screens.update { it.copy(selectedScreen = Screen.Month_Recap) }
                    monthlyRecapView()
                }
                composable(Screen.Records.route) {
                    _Statistics_screens.update { it.copy(selectedScreen = Screen.Records) }
                    recordsView(rantsArray, openRantChat, pads)

                }

            }
        }
    }
}


@Composable
fun monthlyRecapView() {
    Text(text = "monthlyRecapView")
}

@Composable
fun recordsView(
    rantsArray: Array<Array<MutableState<out Any?>>>,
    openRantChat: (id: Int) -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 25.dp)
    ) {
        LazyColumn {
            items(2) { it ->
                if (rantsArray[it][0].value != null) {
                    CardWithImageAndText(
                        rantsArray[it][0].value as RantListTableModel?,
                        rantsArray[it][1].value as Boolean,
                        openRantChat
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardWithImageAndText(
    rant: RantListTableModel?,
    longest: Boolean,
    openRantChat: (id: Int) -> Unit
) {

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
fun createStatsText(rant: RantListTableModel, longest: Boolean) {
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


