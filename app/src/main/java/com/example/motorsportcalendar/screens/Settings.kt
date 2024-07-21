package com.example.motorsportcalendar.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motorsportcalendar.ui.theme.Color5
import com.example.motorsportcalendar.ui.theme.antonFontFamily
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun Settings(viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) { // Use a ViewModel
    val notifyF1 by viewModel.notifyF1.collectAsState()
    val notifyWEC by viewModel.notifyWEC.collectAsState()
    val notifyWRC by viewModel.notifyWRC.collectAsState()
    val notifyMotoGp by viewModel.notifyMotoGP.collectAsState()
    val notifyNASCAR by viewModel.notifyNASCAR.collectAsState()

    val context = LocalContext.current

    // Load the interstitial ad
    val interstitialAdState = remember { mutableStateOf<InterstitialAd?>(null) } // Create MutableState
    LaunchedEffect(Unit) {
        loadInterstitialAd(context as Activity, "ca-app-pub-3847113162973266/2315748820"){
            interstitialAdState.value = it // Update the MutableState
        }
    }

    // Show the ad when showAd is true


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color5)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Notification Settings",
            style = TextStyle(fontSize = 20.sp, fontFamily = antonFontFamily)
        )
        Spacer(modifier = Modifier.height(16.dp))
        SwitchItem(text = "F1 Notifications", checked = notifyF1,interstitialAdState) { newValue ->
            viewModel.setNotifyF1(newValue)
        }
        SwitchItem(text = "WEC Notifications", checked = notifyWEC,interstitialAdState) { newValue ->
            viewModel.setNotifyWEC(newValue)
        }
        SwitchItem(text = "WRC Notifications", checked = notifyWRC,interstitialAdState) { newValue ->
            viewModel.setNotifyWRC(newValue)
        }
        SwitchItem(text = "Moto Gp Notifications", checked = notifyMotoGp,interstitialAdState) { newValue ->
            viewModel.setNotifyMotoGP(newValue)
        }
        SwitchItem(text = "NASCAR Notifications", checked = notifyNASCAR,interstitialAdState) { newValue ->
            viewModel.setNotifyNASCAR(newValue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Notification INFORMATION",
            style = TextStyle(fontSize = 20.sp, fontFamily = antonFontFamily)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "After turning notifications ON You'll receive short notification: \n- 2 Days Before Race Start \n- 24h Before Race Start\n- 1 Hour Before Race Start\n The only AD You'll see in this app appears when u want to change notification button." +
                "I hope That You'll like it.")

    }
}

@Composable
fun SwitchItem(text: String, checked: Boolean, interstitialAdState: MutableState<InterstitialAd?>, onCheckedChange: (Boolean) -> Unit) {
    var isSwitchChecked by remember { mutableStateOf(checked) }
    val showAd = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(vertical = 8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Switch(
            checked = isSwitchChecked,
            onCheckedChange = { newValue ->
                isSwitchChecked = newValue
                onCheckedChange(newValue)
                if (newValue) {
                    showAd.value = true // Trigger the ad when the switch is turned ON
                }
            }
        )
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = showAd.value) {
        if (showAd.value) {
            interstitialAdState.value?.show(context as Activity) // Use interstitialAdState.value
            Log.d("SwitchItem", "Attempting to show ad for: $text")
            showAd.value = false

            // Reload the ad after it's shown
            loadInterstitialAd(context as Activity, "ca-app-pub-3940256099942544/1033173712") { newAd ->
                interstitialAdState.value = newAd // Update the MutableState
            }
        }
    }
}

// Function to load the interstitial ad
fun loadInterstitialAd(activity: Activity, adUnitId: String, onAdLoaded: (InterstitialAd) -> Unit) {
    InterstitialAd.load(
        activity,
        adUnitId,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                onAdLoaded(interstitialAd)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d("AdError", loadAdError.message)
            }
        }
    )
}