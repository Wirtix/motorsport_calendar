package com.example.motorsportcalendar.data.model

import com.google.firebase.Timestamp

data class F1(
    var id: String? = "",
    var category: String? = "",
    var date: Timestamp? = null,
    var name: String? = "",
    var sprint : Boolean = false,
    var fp1_date: Timestamp? = null,
    var fp2_date: Timestamp? = null,
    var fp3_date: Timestamp? = null,
    var quali_date: Timestamp? = null,
    var sprint_date: Timestamp? = null,
    var squali_date: Timestamp? = null,
    var track_url:String? = null,
    var info_url:String? = null,
    var country_flag:String? = null
)
