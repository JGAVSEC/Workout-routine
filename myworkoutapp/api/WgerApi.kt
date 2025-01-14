package com.example.myworkoutapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.myworkoutapp.data.models.ExerciseResponse

interface WgerApi {
    @GET("exercise/search")
    suspend fun searchExercises(
        @Query("term") term: String,
        @Query("language") language: Int = 2  // 2 is English in wger API
    ): ExerciseResponse
}