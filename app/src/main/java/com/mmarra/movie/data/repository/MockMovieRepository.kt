package com.mmarra.movie.data.repository

import com.mmarra.movie.model.Country
import com.mmarra.movie.model.Genre
import com.mmarra.movie.model.Image
import com.mmarra.movie.model.Movie
import com.mmarra.movie.model.Rating
import com.mmarra.movie.model.Sex
import com.mmarra.movie.model.Person
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockMovieRepository @Inject constructor() : MovieRepository {
    override suspend fun getMovies(): List<Movie> {
        delay(1000)
        return listOf(
            Movie(
                id = 1,
                name = "Inception",
                alternativeName = null,
                enName = "Inception",
                year = 2010,
                rating = Rating(kp = 8.7, imdb = 8.8),
                poster = Image(
                    url = "https://play-lh.googleusercontent.com/buKf27Hxendp3tLNpNtP3E-amP0o4yYV-SGKyS2u-Y3GdGRTyfNCIT5WAVs2OudOz6so5K1jtYdAUKI9nw8",
                    previewUrl = "https://play-lh.googleusercontent.com/buKf27Hxendp3tLNpNtP3E-amP0o4yYV-SGKyS2u-Y3GdGRTyfNCIT5WAVs2OudOz6so5K1jtYdAUKI9nw8"
                ),
                genres = listOf(
                    Genre("Sci-Fi"),
                    Genre("Thriller")
                ),
                countries = emptyList(),
                persons = emptyList(),
                description = "A thief who steals corporate secrets through dream-sharing technology.",
                movieLength = 148,
                ageRating = 12
            ),
            Movie(
                id = 2,
                name = "The Matrix",
                alternativeName = null,
                enName = "The Matrix",
                year = 1999,
                rating = Rating(kp = 8.2, imdb = 8.7),
                poster = Image(
                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSA1vgOSJxD6htWgKPPHcwGZfqZ9B5ezYgrFA&s",
                    previewUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSA1vgOSJxD6htWgKPPHcwGZfqZ9B5ezYgrFA&s"
                ),
                genres = listOf(
                    Genre("Action"),
                    Genre("Sci-Fi")
                ),
                countries = emptyList(),
                persons = emptyList(),
                description = "A computer hacker learns from mysterious rebels about the true nature of his reality.",
                movieLength = 136,
                ageRating = 16
            )
        )
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        delay(1000)
        return when (id) {
            1 -> Movie(
                id = 1,
                name = "Inception",
                alternativeName = null,
                enName = "Inception",
                year = 2010,
                rating = Rating(kp = 8.7, imdb = 8.8),
                poster = Image(
                    url = "https://play-lh.googleusercontent.com/buKf27Hxendp3tLNpNtP3E-amP0o4yYV-SGKyS2u-Y3GdGRTyfNCIT5WAVs2OudOz6so5K1jtYdAUKI9nw8",
                    previewUrl = "https://play-lh.googleusercontent.com/buKf27Hxendp3tLNpNtP3E-amP0o4yYV-SGKyS2u-Y3GdGRTyfNCIT5WAVs2OudOz6so5K1jtYdAUKI9nw8"
                ),
                genres = listOf(
                    Genre("Sci-Fi"),
                    Genre("Thriller")
                ),
                countries = listOf(
                    Country("USA"),
                    Country("UK")
                ),
                persons = listOf(
                    Person(
                        1,
                        "Christopher Nolan",
                        "",
                        Sex.MALE,
                        "director"
                    ),
                    Person(
                        2,
                        "Leonardo DiCaprio",
                        "",
                        Sex.MALE,
                        "actor"
                    )
                ),
                description = "A thief who steals corporate secrets through dream-sharing technology.",
                movieLength = 148,
                ageRating = 12
            )

            2 -> Movie(
                id = 2,
                name = "The Matrix",
                alternativeName = null,
                enName = "The Matrix",
                year = 1999,
                rating = Rating(kp = 8.2, imdb = 8.7),
                poster = Image(
                    url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSA1vgOSJxD6htWgKPPHcwGZfqZ9B5ezYgrFA&s",
                    previewUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSA1vgOSJxD6htWgKPPHcwGZfqZ9B5ezYgrFA&s"
                ),
                genres = listOf(
                    Genre("Action"),
                    Genre("Sci-Fi")
                ),
                countries = listOf(Country("USA")),
                persons = listOf(
                    Person(
                        3,
                        "Lana Wachowski",
                        "",
                        Sex.FEMALE,
                        "director"
                    ),
                    Person(
                        4,
                        "Keanu Reeves",
                        "",
                        Sex.MALE,
                        "actor"
                    )
                ),
                description = "A computer hacker learns from mysterious rebels about the true nature of his reality.",
                movieLength = 136,
                ageRating = 16
            )

            else -> null
        }
    }
}