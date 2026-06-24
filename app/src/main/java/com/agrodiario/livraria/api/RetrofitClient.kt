package com.agrodiario.livraria.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var instance: Retrofit

        fun getRetrofitInstance(): Retrofit {

            val httpClient = OkHttpClient.Builder()

            synchronized(this) {

                if (!::instance.isInitialized) {

                    instance = Retrofit.Builder()
                        .client(httpClient.build())
                        .baseUrl("https://openlibrary.org/")
                        .addConverterFactory(
                            GsonConverterFactory.create()
                        )
                        .build()
                }
            }

            return instance
        }

        fun createLivroService(): LivroApiService {
            return getRetrofitInstance()
                .create(LivroApiService::class.java)
        }
    }
}