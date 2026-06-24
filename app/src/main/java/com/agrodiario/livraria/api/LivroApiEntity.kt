package com.agrodiario.livraria.api

import com.google.gson.annotations.SerializedName

data class LivroApiResponse(

    @SerializedName("docs")
    val docs: List<LivroApiEntity>

)

data class LivroApiEntity(

    @SerializedName("title")
    val title: String?,

    @SerializedName("author_name")
    val authorName: List<String>?,

    @SerializedName("cover_i")
    val coverId: Int?

)