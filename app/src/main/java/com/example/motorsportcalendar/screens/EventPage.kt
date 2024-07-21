package com.example.motorsportcalendar.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.motorsportcalendar.R
import com.example.motorsportcalendar.ui.theme.Color1
import com.example.motorsportcalendar.ui.theme.Color5
import com.example.motorsportcalendar.ui.theme.Color6
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventPage(
    sharedViewModel: SharedViewModel,
) {
    val selectedEvent by sharedViewModel.selectedEvent.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color5)
            .padding(10.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        // ... your Column modifiers ...
    ) {
        selectedEvent?.let { event ->
            if( event.category == "WRC" || event.category == "NASCAR") {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color6)
                    .padding(10.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        // ... Display the details of the selected event (name, date, etc.) ...
                        event.name?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .background(Color1)
                            .fillMaxWidth()
                            .height(90.dp)
                    ) {
                        Row(Modifier.padding(8.dp)) {
                            Column(
                                modifier = Modifier.weight(0.2f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Image(painter = painterResource(R.drawable.wrc_icon), contentDescription ="WRC" ,
                                    Modifier
                                        .fillMaxSize()
                                        .padding(10.dp))
                            }
                            Column(modifier = Modifier.weight(0.1f)) {
                                Spacer(
                                    modifier = Modifier
                                        .background(categoryColor(event.category))
                                        .fillMaxHeight()
                                        .width(10.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(0.7f)) {
                                event.date?.let { timestamp ->
                                    val date = timestamp.toDate()
                                    val dateFormatH =
                                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    val dateStringH = dateFormatH.format(date)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row {
                                        Text(
                                            text = "Race week starts:\n $dateStringH",
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
                Box(
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color6)
                        .padding(10.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            // ... Display the details of the selected event (name, date, etc.) ...
                            event.name?.let {
                                Text(
                                    text = it,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .background(Color1)
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
                            Row(Modifier.padding(8.dp)) {
                                Column(
                                    modifier = Modifier.weight(0.2f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    event.fp1_date?.let { timestamp ->
                                        val date = timestamp.toDate()

                                        val dateFormatd = SimpleDateFormat(
                                            "dd",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateFormatm = SimpleDateFormat(
                                            "MMMM",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateStringd = dateFormatd.format(date)
                                        val dateStringm = dateFormatm.format(date)

                                        val shortMonthDateString =
                                            dateStringm.substring(0, 3).uppercase(
                                                Locale.ROOT
                                            )

                                        Text(
                                            text = "$dateStringd\n$shortMonthDateString",
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(0.1f)) {
                                    Spacer(
                                        modifier = Modifier
                                            .background(categoryColor(event.category))
                                            .fillMaxHeight()
                                            .width(10.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(0.7f)) {
                                    event.fp1_date?.let { timestamp ->
                                        val date = timestamp.toDate()
                                        val dateFormatH =
                                            SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val dateStringH = dateFormatH.format(date)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row {
                                            Text(
                                                text = "Free Practice 1: $dateStringH",
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }

                                    if (event.sprint) {
                                        event.squali_date?.let { timestamp ->
                                            val date = timestamp.toDate()
                                            val dateFormatH =
                                                SimpleDateFormat("HH:mm", Locale.getDefault())
                                            val dateStringH = dateFormatH.format(date)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row {
                                                Text(
                                                    text = "Sprint Quali: $dateStringH",
                                                    style = TextStyle(
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        event.fp2_date?.let { timestamp ->

                                            val date = timestamp.toDate()
                                            val dateFormatH =
                                                SimpleDateFormat("HH:mm", Locale.getDefault())
                                            val dateStringH = dateFormatH.format(date)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row {
                                                Text(
                                                    text = "Free Practice 2: $dateStringH",
                                                    style = TextStyle(
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .background(Color1)
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
                            Row(Modifier.padding(8.dp)) {
                                Column(
                                    modifier = Modifier.weight(0.2f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    event.quali_date?.let { timestamp ->
                                        val date = timestamp.toDate()
                                        val dateFormatd = SimpleDateFormat(
                                            "dd",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateFormatm = SimpleDateFormat(
                                            "MMMM",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateStringd = dateFormatd.format(date)
                                        val dateStringm = dateFormatm.format(date)

                                        val shortMonthDateString =
                                            dateStringm.substring(0, 3).uppercase(
                                                Locale.ROOT
                                            )

                                        Text(
                                            text = "$dateStringd\n$shortMonthDateString",
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(0.1f)) {
                                    Spacer(
                                        modifier = Modifier
                                            .background(categoryColor(event.category))
                                            .fillMaxHeight()
                                            .width(10.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(0.7f)) {
                                    event.quali_date?.let { timestamp ->
                                        val date = timestamp.toDate()
                                        val dateFormatH =
                                            SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val dateStringH = dateFormatH.format(date)
                                        Spacer(modifier = Modifier.height(8.dp))


                                        if (event.sprint) {
                                            event.sprint_date?.let { timestamp ->
                                                val date = timestamp.toDate()
                                                val dateFormatH =
                                                    SimpleDateFormat("HH:mm", Locale.getDefault())
                                                val dateStringH = dateFormatH.format(date)
                                                Row {
                                                    Text(
                                                        text = "Sprint: $dateStringH",
                                                        style = TextStyle(
                                                            fontSize = 20.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            event.fp3_date?.let { timestamp ->

                                                val date = timestamp.toDate()
                                                val dateFormatH =
                                                    SimpleDateFormat("HH:mm", Locale.getDefault())
                                                val dateStringH = dateFormatH.format(date)
                                                Row {
                                                    Text(
                                                        text = "Free Practice 3: $dateStringH",
                                                        style = TextStyle(
                                                            fontSize = 20.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }

                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row {
                                            Text(
                                                text = "Qualification: $dateStringH",
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .background(Color1)
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
                            Row(
                                Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.2f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    event.date?.let { timestamp ->
                                        val date = timestamp.toDate()
                                        val dateFormatd = SimpleDateFormat(
                                            "dd",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateFormatm = SimpleDateFormat(
                                            "MMMM",
                                            Locale.getDefault()
                                        ) // Format for day and full month name
                                        val dateStringd = dateFormatd.format(date)
                                        val dateStringm = dateFormatm.format(date)

                                        val shortMonthDateString =
                                            dateStringm.substring(0, 3).toUpperCase(Locale.ROOT)

                                        Text(
                                            text = "$dateStringd\n$shortMonthDateString",
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(0.1f)) {
                                    Spacer(
                                        modifier = Modifier
                                            .background(categoryColor(event.category))
                                            .fillMaxHeight()
                                            .width(10.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(0.7f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    event.date?.let { timestamp ->
                                        val date = timestamp.toDate()
                                        val dateFormatH =
                                            SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val dateStringH = dateFormatH.format(date)
                                        Row(
                                            Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Race: $dateStringH",
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            )
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val websiteUrl = event.info_url // Replace with your actual URL

            val openWebsiteIntent = remember {
                Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            }
            Button(
                onClick = {
                    startActivity(context, openWebsiteIntent, null)
                }, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color6,
                    contentColor = Color.Black,
                )

            ) {
                Text("More Informations ")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (event.track_url != "") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight().clip(RoundedCornerShape(16.dp))
                        .background(Color1)
                        .padding(8.dp)

                ) {
                    val imageUrl = event.track_url
                    GlideImage(
                        model = imageUrl,
                        contentDescription = "Image description",
                        modifier = Modifier.fillMaxSize(),

                        )
                }

            }
        }
    }
}
