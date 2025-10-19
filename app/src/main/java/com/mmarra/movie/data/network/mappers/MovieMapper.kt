package com.mmarra.movie.data.network.mappers

import com.mmarra.movie.data.network.dto.ImageDto
import com.mmarra.movie.data.network.dto.MovieDto
import com.mmarra.movie.data.network.dto.MovieObjectResponse
import com.mmarra.movie.data.network.dto.PersonDto
import com.mmarra.movie.data.network.dto.RatingDto
import com.mmarra.movie.data.network.dto.SimilarMovieDto
import com.mmarra.movie.data.network.dto.TrailerDto
import com.mmarra.movie.data.network.dto.VideosDto
import com.mmarra.movie.model.Country
import com.mmarra.movie.model.Genre
import com.mmarra.movie.model.Image
import com.mmarra.movie.model.Movie
import com.mmarra.movie.model.Name
import com.mmarra.movie.model.Person
import com.mmarra.movie.model.Rating
import com.mmarra.movie.model.Sex
import com.mmarra.movie.model.SimilarMovie
import com.mmarra.movie.model.Trailer
import com.mmarra.movie.model.Videos
import java.time.Instant
import java.time.format.DateTimeParseException

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    name = name,
    alternativeName = alternativeName,
    enName = enName,
    names = names.map { Name(it.name, it.language, it.type) },
    year = year,
    description = description,
    shortDescription = null,
    slogan = null,
    status = null,
    rating = rating?.toDomain(),
    poster = poster?.toDomain(),
    logo = logo?.toDomain(),
    backdrop = backdrop?.toDomain(),
    videos = videos?.toDomain(),
    genres = genres.map { Genre(it.name) },
    countries = countries.map { Country(it.name) },
    persons = persons.map { it.toDomain() },
    movieLength = movieLength,
    ageRating = ageRating,
    similarMovies = similarMovies.map { it.toDomain() },
    top10 = top10,
    top250 = top250,
    updatedAt = updatedAt.toInstantOrNull(),
    createdAt = createdAt.toInstantOrNull()
)

fun MovieObjectResponse.toDomain(): Movie = Movie(
    id = id,
    name = name,
    alternativeName = alternativeName,
    enName = enName,
    names = names.map { Name(it.name, it.language, it.type) },
    year = year,
    description = description,
    shortDescription = null,
    slogan = null,
    status = null,
    rating = rating?.toDomain(),
    poster = poster?.toDomain(),
    logo = logo?.toDomain(),
    backdrop = backdrop?.toDomain(),
    videos = videos?.toDomain(),
    genres = genres.map { Genre(it.name) },
    countries = countries.map { Country(it.name) },
    persons = persons.map { it.toDomain() },
    movieLength = movieLength,
    ageRating = ageRating,
    similarMovies = similarMovies.map { it.toDomain() },
    top10 = top10,
    top250 = top250,
    updatedAt = updatedAt.toInstantOrNull(),
    createdAt = createdAt.toInstantOrNull()
)

fun RatingDto.toDomain(): Rating = Rating(
    kp = kp,
    imdb = imdb,
    tmdb = tmdb,
    filmCritics = filmCritics,
    russianFilmCritics = russianFilmCritics,
    await = await
)

fun ImageDto.toDomain(): Image = Image(url = url, previewUrl = previewUrl)

fun TrailerDto.toDomain(): Trailer =
    Trailer(url = url, name = name, site = site, size = size, type = type)

fun VideosDto.toDomain(): Videos = Videos(trailers = trailers.map { it.toDomain() })

fun SimilarMovieDto.toDomain(): SimilarMovie =
    SimilarMovie(
        id = id,
        name = name,
        enName = enName,
        alternativeName = alternativeName,
        poster = poster?.toDomain(),
        rating = rating?.toDomain(),
        year = year
    )

fun PersonDto.toDomain(): Person {
    val sexEnum = when (sex?.uppercase()) {
        "MALE" -> Sex.MALE
        "FEMALE" -> Sex.FEMALE
        else -> null
    }
    return Person(
        id = id,
        name = name,
        enName = enName,
        photo = photo,
        profession = profession,
        sex = sexEnum
    )
}

private fun String?.toInstantOrNull(): Instant? =
    try {
        this?.let { Instant.parse(it) }
    } catch (e: DateTimeParseException) {
        null
    }
