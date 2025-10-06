package com.mmarra.movie.model

data class Movie(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val enName: String?,
    val year: Int?,
    val description: String?,
    val rating: Rating?,
    val poster: Image?,
    val genres: List<Genre>,
    val countries: List<Country>,
    val persons: List<Person>,
    val movieLength: Int?,
    val ageRating: Int?
)

data class Rating(
    val kp: Double?,
    val imdb: Double?
)

data class Genre(val name: String)

data class Country(val name: String)

data class Person(
    val id: Int,
    val name: String,
    val photo: String?,
    val sex: Sex,
    val profession: String?
)

enum class Sex {
    MALE, FEMALE
}

data class Image(
    val url: String?,
    val previewUrl: String?
)