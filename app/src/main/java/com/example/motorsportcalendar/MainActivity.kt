

package com.example.motorsportcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf

import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.motorsportcalendar.notifications.RaceNotificationWorker

import com.example.motorsportcalendar.screens.EventPage
import com.example.motorsportcalendar.screens.Home
import com.example.motorsportcalendar.screens.Screens
import com.example.motorsportcalendar.screens.Settings
import com.example.motorsportcalendar.screens.SharedViewModel
import com.example.motorsportcalendar.ui.theme.Color1
import com.example.motorsportcalendar.ui.theme.Color6
import com.example.motorsportcalendar.ui.theme.MotorsportCalendarTheme
import com.example.motorsportcalendar.ui.theme.antonFontFamily
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import com.google.firebase.FirebaseApp
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        scheduleRaceNotificationWorker(applicationContext)
        setContent {
            MotorsportCalendarTheme {
                Surface {

                    val sharedViewModel: SharedViewModel = viewModel() // Provide ViewModel
                    LearnNavBotSheet(sharedViewModel) // Pass to LearnNavBotSheet
                }
            }
        }
    }
}
private fun scheduleRaceNotificationWorker(context: Context) {
    Log.d("NOTTEST", "schedule started")
    val constraints = Constraints.Builder()
        // Add any necessary constraints (e.g., network availability)
        .build()

    val raceNotificationWorkRequest = PeriodicWorkRequestBuilder<RaceNotificationWorker>(
        15, TimeUnit.MINUTES // Repeat every 6 hours
    ).setConstraints(constraints).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "raceNotificationWork", // Unique work name
        ExistingPeriodicWorkPolicy.UPDATE, // Keep existing work if it exists
        raceNotificationWorkRequest
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnNavBotSheet(sharedViewModel: SharedViewModel){

    val navigationController = rememberNavController()

    val selected = remember {
        mutableIntStateOf(R.drawable.calendar_icon)
    }
        Scaffold(
            topBar = {
                TopAppBar(title={ Text(text = "Motorsport Calendar", fontFamily = antonFontFamily)},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color1,
                        titleContentColor = Color6,
                        navigationIconContentColor = Color6
                    ),
                    actions = { // Add the settings button here
                        IconButton(onClick = {
                            // Navigate to your Settings screen
                            navigationController.navigate("settings")
                            selected.intValue = 1
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Settings, // Or any other suitable icon
                                contentDescription = "Settings",
                                tint = Color6
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color1,
                    modifier = Modifier.height(100.dp)
                ){
                    IconButton(
                        onClick = {
                            selected.intValue = R.drawable.calendar_icon
                            navigationController.navigate(Screens.Home.screen){
                                launchSingleTop = true
                            }

                        },
                        modifier =Modifier.weight(1f)) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_icon), contentDescription = null, modifier = Modifier.size(30.dp),
                            tint = if(selected.intValue == R.drawable.calendar_icon) Color.White else Color6

                        )
                    }
                    IconButton(
                        onClick = {
                            selected.intValue = R.drawable.historical_icon
                            navigationController.navigate(Screens.Historicals.screen){
                                launchSingleTop = true
                            }

                        },
                        modifier =Modifier.weight(1f)) {
                        Icon(
                            painter = painterResource(id = R.drawable.historical_icon), contentDescription = null, modifier = Modifier.size(30.dp),
                            tint = if(selected.intValue == R.drawable.historical_icon) Color.White else Color6

                        )
                    }


                    IconButton(
                        onClick = {
                            selected.intValue = R.drawable.round_notifications_24
                            navigationController.navigate(Screens.Settings.screen){
                                launchSingleTop = true
                            }

                        },
                        modifier =Modifier.weight(1f)) {
                        Icon(
                            painter = painterResource(id =R.drawable.round_notifications_24), contentDescription = null, modifier = Modifier.size(30.dp),
                            tint = if(selected.intValue == R.drawable.round_notifications_24) Color.White else Color6

                        )
                    }

                }
            }
        ) {paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                // AdBanner at the top
                BannerAd()

            NavHost(navController = navigationController,
                startDestination = Screens.Home.screen)
            {
                composable(Screens.Home.screen) { Home(navigationController, sharedViewModel, true) }
                composable(Screens.Historicals.screen) { Home(navigationController, sharedViewModel, false) }
                composable(Screens.EventPage.screen) { EventPage(sharedViewModel)}
                composable(Screens.Settings.screen){ Settings()}
            }


        }
    }
}
@Composable
fun BannerAd(modifier: Modifier = Modifier){
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3847113162973266/7121869866"
                loadAd(AdRequest.Builder().build())
            }
        })
}




