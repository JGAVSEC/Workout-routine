package com.example.myworkoutapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.myworkoutapp.data.models.ExerciseResponse

interface WgerApi {
    @GET("exercise/search/")
    suspend fun searchExercises(
        @Query("language") language: String = "en",
        @Query("term") term: String
    ): ExerciseResponse
}