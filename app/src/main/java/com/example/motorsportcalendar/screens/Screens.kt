package com.example.motorsportcalendar.screens

sealed class Screens(val screen:String) {
    data object  Home:Screens("home")
    data object  Historicals:Screens("search")
    data object EventPage : Screens("event_details")
    data object Settings : Screens("settings")

}