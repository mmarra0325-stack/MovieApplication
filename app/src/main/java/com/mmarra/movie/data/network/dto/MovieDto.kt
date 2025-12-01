package com.mmarra.movie.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MovieObjectResponse(
    val id: Int,
    val name: String? = null,
    @SerialName("alternativeName") val alternativeName: String? = null,
    @SerialName("enName") val enName: String? = null,
    val names: List<NameDto> = emptyList(),
    val year: Int? = null,
    val description: String? = null,
    val rating: RatingDto? = null,
    val poster: ImageDto? = null,
    val logo: ImageDto? = null,
    val backdrop: ImageDto? = null,
    val videos: VideosDto? = null,
    val genres: List<GenreDto> = emptyList(),
    val countries: List<CountryDto> = emptyList(),
    val persons: List<PersonDto> = emptyList(),
    @SerialName("movieLength") val movieLength: Int? = null,
    @SerialName("ageRating") val ageRating: Int? = null,
    val similarMovies: List<SimilarMovieDto> = emptyList(),
    val top10: Int? = null,
    val top250: Int? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null,
    @SerialName("externalId") val externalId: ExternalIdDto? = null
)

@Serializable
data class MovieListResponse(
    val docs: List<MovieDto> = emptyList(),
    val total: Int = 0
)

@Serializable
data class MovieDto(
    val id: Int,
    val name: String? = null,
    @SerialName("alternativeName") val alternativeName: String? = null,
    @SerialName("enName") val enName: String? = null,
    val names: List<NameDto> = emptyList(),
    val year: Int? = null,
    val description: String? = null,
    val rating: RatingDto? = null,
    val poster: ImageDto? = null,
    val logo: ImageDto? = null,
    val backdrop: ImageDto? = null,
    val videos: VideosDto? = null,
    val genres: List<GenreDto> = emptyList(),
    val countries: List<CountryDto> = emptyList(),
    val persons: List<PersonDto> = emptyList(),
    @SerialName("movieLength") val movieLength: Int? = null,
    @SerialName("ageRating") val ageRating: Int? = null,
    val similarMovies: List<SimilarMovieDto> = emptyList(),
    val top10: Int? = null,
    val top250: Int? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null,
    @SerialName("externalId") val externalId: ExternalIdDto? = null
)

@Serializable
data class ExternalIdDto(val kpHD: String? = null, val imdb: String? = null, val tmdb: Int? = null)

@Serializable
data class NameDto(val name: String, val language: String? = null, val type: String? = null)

@Serializable
data class RatingDto(
    val kp: Double? = null,
    val imdb: Double? = null,
    val tmdb: Double? = null,
    val filmCritics: Double? = null,
    @SerialName("russianFilmCritics") val russianFilmCritics: Double? = null,
    val await: Double? = null
)

@Serializable
data class ImageDto(val url: String? = null, val previewUrl: String? = null)

@Serializable
data class GenreDto(val name: String)

@Serializable
data class CountryDto(val name: String)

@Serializable
data class PersonDto(
    val id: Int,
    val name: String,
    val photo: String? = null,
    @SerialName("enName") val enName: String? = null,
    val profession: String? = null,
    val sex: String? = null
)

@Serializable
data class TrailerDto(
    val url: String? = null,
    val name: String? = null,
    val site: String? = null,
    val size: Int? = null,
    val type: String? = null
)

@Serializable
data class VideosDto(val trailers: List<TrailerDto> = emptyList())

@Serializable
data class SimilarMovieDto(
    val id: Int,
    val name: String? = null,
    @SerialName("alternativeName") val alternativeName: String? = null,
    @SerialName("enName") val enName: String? = null,
    val poster: ImageDto? = null,
    val rating: RatingDto? = null,
    val year: Int? = null
)
