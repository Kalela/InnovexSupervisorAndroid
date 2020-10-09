package com.kalela.innovexsupervisor.data.model

import com.google.firebase.Timestamp

data class Task(
    val name: String = "",
    val actual_time: String = "",
    var actualTimeAsString : String = "",
    val application_time: String = "",
    val task_definition: String = ""
)