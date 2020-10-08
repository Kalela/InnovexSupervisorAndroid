package com.kalela.innovexsupervisor.data.model

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

data class Task(
    val name: String = "",
    val actual_time: Timestamp = Timestamp(0,0),
    val application_time: String = ""
)