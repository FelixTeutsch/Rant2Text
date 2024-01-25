package it.teutsch.felix.rant2text.ui.view.Statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.ui.model.RantViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyRecap(
    rantViewModel: RantViewModel,
    innerPadding: PaddingValues,
    openRantChat: (id: Int) -> Unit,
    scrollNavigation: Boolean,
) {
    val calenderState = rememberSheetState()

    var selectedDate by remember { mutableStateOf("") }

    var dayStart by remember { mutableLongStateOf(0L) }
    var dayEnd by remember { mutableLongStateOf(0L) }

    var rantsOnSelectedDate by remember { mutableStateOf(emptyList<RantListTableModel>()) }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        CalendarDialog(
            state = calenderState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date { date ->
                dayStart = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                dayEnd = date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()

                val formatter = DateTimeFormatter.ofPattern("EEEE dd.MM", Locale.getDefault())
                val formattedDate = date.format(formatter)

                selectedDate = formattedDate
            },
        )

//        if (scrollNavigation) {
//            Row (
//                modifier = Modifier
//                    .fillMaxWidth(),
//
//                horizontalArrangement = Arrangement.Center
//            ){
//
//            }
//            Text(
//                text = "Monthly Recap",
//                modifier = Modifier
//                    .padding(horizontal = 35.dp)
//                    .padding(top = 15.dp),
//                color = Color.White
//            )
//        }

        Button(
            onClick = {
                calenderState.show()
            },
            modifier = Modifier
                .width(200.dp)
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally),


            ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar Icon")
            androidx.compose.material3.Text(text = "Pick A Date")
        }

        LaunchedEffect(key1 = dayStart, key2 = dayEnd, key3 = selectedDate) {
            rantViewModel.getRantsOnDate(dayStart, dayEnd).collect { rants ->
                rantsOnSelectedDate = rants
            }


        }

        createRantsCard(rantsOnSelectedDate, openRantChat, selectedDate)

    }

}

@Composable
fun createRantsCard(
    rants: List<RantListTableModel>,
    openRantChat: (id: Int) -> Unit,
    selectedDate: String
) {
    if (rants.isNotEmpty()) {
        Text(
            text = selectedDate,
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .padding(top = 20.dp),
            color = Color.White
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp),

            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {

            items(rants.size) { index ->
                ElevatedCard(
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable(
                            onClick = {
                                openRantChat(rants[index].id)
                            }

                        ),
//

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // This line aligns the Icon to the right
                    ) {
                        Box(
                            modifier = Modifier
//                                .weight(0.15f)
                                .size(43.dp)
                                .background(rants[index].angerLevel.angerColor, CircleShape)
                                .padding(end = 10.dp),
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.7f)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = "${rants[index].title}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${rants[index].text}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "icon for opening the chat",
                            modifier = Modifier
                                .weight(0.10f)
                                .size(15.dp)
                                .padding(horizontal = 7.dp)
                                .rotate(180f)
//                                .border(1.dp, Color.White),
                        )

                    }
                }


            }
        }
    }
}