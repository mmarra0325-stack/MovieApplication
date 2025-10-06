package com.mmarra.movie.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Movies

@Serializable
data class MovieDetail(val id: Int)
