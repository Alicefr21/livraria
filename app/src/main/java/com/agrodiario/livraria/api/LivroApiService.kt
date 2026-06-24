package com.agrodiario.livraria.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LivroApiService {

    @GET("search.json")
    fun buscarLivros(
        @Query("q") termo: String
    ): Call<LivroApiResponse>

}