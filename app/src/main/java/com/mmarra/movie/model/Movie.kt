package com.mmarra.movie.model

import java.time.Instant

data class Movie(
    val id: Int,
    val name: String? = null,
    val alternativeName: String? = null,
    val enName: String? = null,
    val names: List<Name> = emptyList(),
    val year: Int? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val slogan: String? = null,
    val status: String? = null,
    val rating: Rating? = null,
    val poster: Image? = null,
    val logo: Image? = null,
    val backdrop: Image? = null,
    val videos: Videos? = null,
    val genres: List<Genre> = emptyList(),
    val countries: List<Country> = emptyList(),
    val persons: List<Person> = emptyList(),
    val movieLength: Int? = null,
    val ageRating: Int? = null,
    val similarMovies: List<SimilarMovie> = emptyList(),
    val top10: Int? = null,
    val top250: Int? = null,
    val updatedAt: Instant? = null,
    val createdAt: Instant? = null
)

data class Name(val name: String, val language: String? = null, val type: String? = null)

data class Rating(
    val kp: Double? = null,
    val imdb: Double? = null,
    val tmdb: Double? = null,
    val filmCritics: Double? = null,
    val russianFilmCritics: Double? = null,
    val await: Double? = null
)

data class Image(val url: String? = null, val previewUrl: String? = null)

data class Genre(val name: String)

data class Country(val name: String)

data class Person(
    val id: Int,
    val name: String,
    val enName: String? = null,
    val photo: String? = null,
    val profession: String? = null,
    val sex: Sex? = null
)

enum class Sex { MALE, FEMALE }

data class Trailer(val url: String? = null, val name: String? = null, val site: String? = null, val size: Int? = null, val type: String? = null)

data class Videos(val trailers: List<Trailer> = emptyList())

data class SimilarMovie(
    val id: Int,
    val name: String? = null,
    val enName: String? = null,
    val alternativeName: String? = null,
    val poster: Image? = null,
    val rating: Rating? = null,
    val year: Int? = null
)

fun Movie.displayTitle(): String =
    name?.takeIf { it.isNotBlank() }
        ?: alternativeName?.takeIf { it.isNotBlank() }
        ?: enName?.takeIf { it.isNotBlank() }
        ?: names.firstOrNull()?.name
        ?: "Без названия"
