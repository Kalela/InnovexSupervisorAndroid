package com.kalela.innovexsupervisor.data.model

import com.google.firebase.Timestamp

data class Task(
    val name: String = "",
    val actual_time: Timestamp = Timestamp(0,0),
    var actualTimeAsString : String = "",
//    val application_time: Timestamp = Timestamp(0,0),
    val task_definition: String = ""
)