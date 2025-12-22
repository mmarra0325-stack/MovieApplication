package com.mmarra.domain.model

data class MovieFilters(
    val genre: String = "",
    val year: Int? = null,
    val rating: Float? = null,
)
