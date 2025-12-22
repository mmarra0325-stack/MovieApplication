package com.mmarra.domain.model

data class Profile(
    val username: String,
    val job: String,
    val photoUri: String,
    val resumeUrl: String,
    val notificationTime: String,
)