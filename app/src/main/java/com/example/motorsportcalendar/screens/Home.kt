@file:Suppress("DEPRECATION")

package com.example.motorsportcalendar.screens


import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.motorsportcalendar.R
import com.example.motorsportcalendar.ui.theme.Color1
import com.example.motorsportcalendar.ui.theme.Color2
import com.example.motorsportcalendar.ui.theme.Color4
import com.example.motorsportcalendar.ui.theme.Color5
import com.example.motorsportcalendar.ui.theme.Color6
import com.example.motorsportcalendar.ui.theme.Color7
import com.example.motorsportcalendar.ui.theme.Color8
import com.example.motorsportcalendar.ui.theme.antonFontFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

data class FilterItem(val name: String, val imageResId: Int)


@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnrememberedMutableState", "DefaultLocale")
@Composable
fun Home(navController: NavHostController, sharedViewModel: SharedViewModel, current: Boolean) {


    val filterList = listOf(
        FilterItem("All", R.drawable.all_icon), // Replace with your actual drawable
        FilterItem("F1", R.drawable.f1_black_logo),
        FilterItem("WEC", R.drawable.wec_icon), // Replace with your actual drawable
        FilterItem("WRC", R.drawable.wrc_icon),
        FilterItem("motoGP", R.drawable.motogp_icon),
        FilterItem("NASCAR", R.drawable.nascar_icon)// Replace with your actual drawable
    )


    val context = LocalContext.current

    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
    val homeViewModel: HomeViewModel = viewModel()
    val raceList by homeViewModel.raceList.observeAsState(emptyList())

    val selectedFilterCategory by sharedViewModel.selectedFilterCategory.observeAsState("All")

    var filterCategory by remember { mutableStateOf(selectedFilterCategory) }
    LaunchedEffect(key1 = sharedViewModel.selectedFilterCategory) {
        sharedViewModel.selectedFilterCategory.value?.let {
            filterCategory = it
        }
    }

    // on below line creating variable for list of data.
    // on below line creating variable for freebase database
    // and database reference.

    // on below line getting data from our database

    val filteredCourseList by remember(filterCategory, raceList) {
        derivedStateOf {
            val calendarToday = Calendar.getInstance()
            if (current) {
                if (filterCategory == "All") {
                    raceList.filter { it?.date?.toDate()?.after(calendarToday.time) == true }
                } else {
                    raceList.filter {
                        it?.category?.toLowerCase() == filterCategory.toLowerCase() &&
                                it.date?.toDate()?.after(calendarToday.time) == true
                    }
                }
            }else{
                if (filterCategory == "All") {
                    raceList.filter { it?.date?.toDate()?.before(calendarToday.time) == true }
                } else {
                    raceList.filter {
                        it?.category?.toLowerCase() == filterCategory.toLowerCase() &&
                                it.date?.toDate()?.before(calendarToday.time) == true
                    }
                }
            }
        }
    }

    // on below line creating a column
    // to display our retrieved list.
    Column(
        // adding modifier for our column
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color5),
        // on below line adding vertical and
        // horizontal alignment for column.
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color2)) {
            items(filterList) { filter ->
                Card(
                    onClick = {
                        // inside on click we are
                        // displaying the toast message.

                        filterCategory = filter.name
                        sharedViewModel.updateFilterCategory(filter.name)
                       },
                    // on below line we are adding
                    // padding from our all sides.
                    modifier = Modifier
                        .padding(8.dp)
                        .height(30.dp)
                        .width(50.dp)



                    // on below line we are adding
                    // elevation for the card.
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Image(painter = painterResource(id = filter.imageResId), contentDescription = filter.name ,
                            modifier = Modifier
                                .width(50.dp)
                                .height(40.dp)
                                .background(Color2))
                    }
                }
            }
        }

        // on below line we are
        // calling lazy column
        // for displaying listview.
        LazyColumn {
            // on below line we are setting data
            // for each item of our listview.
            itemsIndexed(filteredCourseList) { index, event ->
                LaunchedEffect(filteredCourseList) { // Depends on the filtered list
                    Log.d("FilteredList", filteredCourseList.toString())
                }
                // on below line we are creating
                // a card for our list view item.
                if (index != 0 || !current) {
                    Card(
                        onClick = {
                            event?.let {
                                sharedViewModel.setSelectedEvent(it) // Set selected event
                                navController.navigate(Screens.EventPage.screen) // Navigate
                                // inside on click we are
                                // displaying the toast message.

                            }
                        },
                        // on below line we are adding
                        // padding from our all sides.
                        modifier = Modifier
                            .padding(8.dp)
                            .height(130.dp)

                        // on below line we are adding
                        // elevation for the card.
                    ) {
                        // on below line we are creating
                        // a row for our list view item.
                        Row(
                            // for our row we are adding modifier
                            // to set padding from all sides.
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .background(Color1)

                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                filteredCourseList[index]?.category?.let {
                                    when (filteredCourseList[index]?.category) {
                                        "F1" -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.f1_logo),
                                                contentDescription = "F1 Icon"
                                            )
                                        }
                                        "WEC" -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.wec_icon),
                                                contentDescription = "WEC Icon",
                                                modifier = Modifier
                                                    .width(60.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                        "MotoGP" -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.motogp_icon),
                                                contentDescription = "MOTOGP Icon",
                                                modifier = Modifier
                                                    .width(60.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                        "WRC" -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.wrc_icon),
                                                contentDescription = "WRC Icon",
                                                modifier = Modifier
                                                    .width(60.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                        "NASCAR" -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.nascar_icon),
                                                contentDescription = "NASCAR Icon",
                                                modifier = Modifier
                                                    .width(60.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.weight(0.75f),
                                Arrangement.Center,
                                Alignment.CenterHorizontally
                            ) {

                                // on below line inside row we are adding spacer
                                // on below line we are displaying course name.
                                filteredCourseList[index]?.name?.let {
                                    Text(
                                        // inside the text on below line we are
                                        // setting text as the language name
                                        // from our modal class.
                                        text = it,

                                        // on below line we are adding padding
                                        // for our text from all sides.
                                        modifier = Modifier.padding(4.dp),

                                        // on below line we are adding
                                        // color for our text
                                        color = Color6,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 22.sp, fontFamily = antonFontFamily
                                        )
                                    )
                                }
                                // adding spacer on below line.

                                // on below line displaying text for course duration
                                filteredCourseList[index]?.date?.let { timestamp ->
                                    val calendarEnd = Calendar.getInstance()
                                    calendarEnd.time = timestamp.toDate()

                                    val calendarStart = Calendar.getInstance()
                                    calendarStart.time = timestamp.toDate()
                                    calendarStart.add(
                                        Calendar.DAY_OF_MONTH,
                                        -2
                                    ) // go back 2 days for the end date

                                    val dateFormat =
                                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    val startDateString = String.format(
                                        "%02d",
                                        calendarStart.get(Calendar.DAY_OF_MONTH)
                                    )
                                    val endDateString = dateFormat.format(calendarEnd.time)

                                    val dateRangeString = "$startDateString-${endDateString}"
                                    Text(
                                        text = dateRangeString,
                                        modifier = Modifier.padding(4.dp), color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(fontSize = 20.sp)
                                    )
                                }
                                // adding spacer on below line.
                                // on below line displaying text for course description
                                filteredCourseList[index]?.date?.let { timestamp ->
                                    val calendarEventEnd = Calendar.getInstance()
                                    calendarEventEnd.time = timestamp.toDate()
                                    calendarEventEnd.add(
                                        Calendar.DAY_OF_MONTH,
                                        2
                                    ) // Add 2 days to get the end date

                                    val calendarToday = Calendar.getInstance()

                                    val diffInMillis =
                                        calendarEventEnd.timeInMillis - calendarToday.timeInMillis
                                    val daysLeft =
                                        TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)

                                    Text(
                                        text = if (current){"Days left: $daysLeft"}else{"Days ago: ${daysLeft*-1}"}, // Display the days left
                                        modifier = Modifier.padding(4.dp),
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(fontSize = 15.sp)
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.15f)
                                    .fillMaxHeight()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {val svgUrl = event?.country_flag // Get SVG URL from Firestore data
                                Log.d("SVGDebug", "SVG URL: $svgUrl")
                                svgUrl?.let {url ->

                                    val painter = rememberAsyncImagePainter(
                                        model = ImageRequest.Builder(context)

                                            .decoderFactory(SvgDecoder.Factory())
                                            .data(url) // Load SVG from URL
                                            .size(Size(100, 80))
                                            .build(),
                                        onError = { _ ->
                                            Log.e("SVGDebug", "Error loading SVG")
                                            // ...
                                        }
                                    )
                                    Image(
                                        painter = painter,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }else if (event?.category=="WRC" || event?.category=="NASCAR" ) {
                    Card(
                        onClick = {
                            event.let {
                                sharedViewModel.setSelectedEvent(it) // Set selected event
                                navController.navigate(Screens.EventPage.screen) // Navigate
                                // inside on click we are
                                // displaying the toast message.
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(Color6)
                                .padding(10.dp)

                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // ... Display the details of the selected event (name, date, etc.) ...
                                event.name?.let {
                                    Text(
                                        text = it,
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = antonFontFamily
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
                                        if (event.category=="WRC") {
                                            Image(
                                                painter = painterResource(R.drawable.wrc_icon),
                                                contentDescription = "WRC",
                                                Modifier
                                                    .fillMaxSize()
                                                    .padding(10.dp)
                                            )
                                        }
                                        else{
                                            Image(
                                                painter = painterResource(R.drawable.nascar_icon),
                                                contentDescription = "NASCAR",
                                                Modifier
                                                    .fillMaxSize()
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.1f)) {
                                        Spacer(
                                            modifier = Modifier
                                                .background(Color4)
                                                .fillMaxHeight()
                                                .width(10.dp)
                                        )
                                    }
                                    Column(modifier = Modifier
                                        .weight(0.7f)
                                        .fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                                        event.date?.let { timestamp ->
                                            val date = timestamp.toDate()
                                            val dateFormatH =
                                                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                            val dateStringH = dateFormatH.format(date)
                                            Spacer(modifier = Modifier.height(20.dp))
                                            Row {
                                                Text(modifier = Modifier.fillMaxSize(),
                                                    text = "STARTS AT...$dateStringH",
                                                    style = TextStyle(
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color6

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


                else{
                    Card(
                        onClick = {
                            event?.let {
                                sharedViewModel.setSelectedEvent(it) // Set selected event
                                navController.navigate(Screens.EventPage.screen) // Navigate
                                // inside on click we are
                                // displaying the toast message.
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(Color6)
                                .padding(10.dp)

                        ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                // ... Display the details of the selected event (name, date, etc.) ...
                                if (event != null) {
                                    event.name?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = antonFontFamily
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        val svgUrl = event?.country_flag // Get SVG URL from Firestore data
                                            Log.d("SVGDebug", "SVG URL: $svgUrl")
                                            svgUrl?.let {url ->

                                                val painter = rememberAsyncImagePainter(
                                                    model = ImageRequest.Builder(context)

                                                        .decoderFactory(SvgDecoder.Factory())
                                                        .data(url) // Load SVG from URL
                                                        .size(Size(100, 80))
                                                        .build(),
                                                    onError = { _ ->
                                                        Log.e("SVGDebug", "Error loading SVG")
                                                        // ...
                                                    }
                                                )
                                                Image(
                                                    painter = painter,
                                                    contentDescription = null,
                                                )
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
                                        event?.fp1_date?.let { timestamp ->
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
                                                dateStringm.substring(0, 3).toUpperCase(
                                                    Locale.ROOT
                                                )

                                            Text(
                                                text = "$dateStringd\n$shortMonthDateString",
                                                style = TextStyle(
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color6
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.1f)) {
                                        Spacer(
                                            modifier = Modifier
                                                .background(categoryColor(event?.category))
                                                .fillMaxHeight()
                                                .width(10.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(0.7f)) {
                                        if (event != null) {
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
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color6
                                                        )
                                                    )
                                                }
                                            }
                                        }

                                        if (event != null) {
                                            if (event.sprint) {
                                                event.squali_date?.let { timestamp ->
                                                    val date = timestamp.toDate()
                                                    val dateFormatH =
                                                        SimpleDateFormat(
                                                            "HH:mm",
                                                            Locale.getDefault()
                                                        )
                                                    val dateStringH = dateFormatH.format(date)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Row {
                                                        Text(
                                                            text = "Sprint Quali: $dateStringH",
                                                            style = TextStyle(
                                                                fontSize = 20.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color6
                                                            )
                                                        )
                                                    }
                                                }
                                            } else {
                                                event.fp2_date?.let { timestamp ->

                                                    val date = timestamp.toDate()
                                                    val dateFormatH =
                                                        SimpleDateFormat(
                                                            "HH:mm",
                                                            Locale.getDefault()
                                                        )
                                                    val dateStringH = dateFormatH.format(date)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Row {
                                                        Text(
                                                            text = "Free Practice 2: $dateStringH",
                                                            style = TextStyle(
                                                                fontSize = 20.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color6
                                                            )
                                                        )
                                                    }
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
                                        if (event != null) {
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
                                                    dateStringm.substring(0, 3).toUpperCase(
                                                        Locale.ROOT
                                                    )

                                                Text(
                                                    text = "$dateStringd\n$shortMonthDateString",
                                                    style = TextStyle(
                                                        fontSize = 24.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color6
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.1f)) {
                                        Spacer(
                                            modifier = Modifier
                                                .background(categoryColor(event?.category))
                                                .fillMaxHeight()
                                                .width(10.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(0.7f)) {
                                        if (event != null) {
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
                                                            SimpleDateFormat(
                                                                "HH:mm",
                                                                Locale.getDefault()
                                                            )
                                                        val dateStringH = dateFormatH.format(date)
                                                        Row {
                                                            Text(
                                                                text = "Sprint: $dateStringH",
                                                                style = TextStyle(
                                                                    fontSize = 20.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color6
                                                                )
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    event.fp3_date?.let { timestamp ->

                                                        val date = timestamp.toDate()
                                                        val dateFormatH =
                                                            SimpleDateFormat(
                                                                "HH:mm",
                                                                Locale.getDefault()
                                                            )
                                                        val dateStringH = dateFormatH.format(date)
                                                        Row {
                                                            Text(
                                                                text = "Free Practice 3: $dateStringH",
                                                                style = TextStyle(
                                                                    fontSize = 20.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color6
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
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color6
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
                                Row(
                                    Modifier
                                        .padding(8.dp)
                                        .fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier.weight(0.2f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (event != null) {
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
                                                    dateStringm.substring(0, 3)
                                                        .toUpperCase(Locale.ROOT)

                                                Text(
                                                    text = "$dateStringd\n$shortMonthDateString",
                                                    style = TextStyle(
                                                        fontSize = 24.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color6
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.1f)) {
                                        Spacer(
                                            modifier = Modifier
                                                .background(categoryColor(event?.category))
                                                .fillMaxHeight()
                                                .width(10.dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.weight(0.7f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        if (event != null) {
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
                                                            color = Color6
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
                }
            }

            }
        }
    }

public fun categoryColor(category: String?): Color {
    if(category=="F1"){
        return Color7
    }
    if(category=="WEC"){
        return Color8
    }
    return Color4
}


