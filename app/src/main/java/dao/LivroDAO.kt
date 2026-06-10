package com.agrodiario.livraria.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.agrodiario.livraria.entity.LivroEntity

@Dao
interface LivroDAO {

    @Insert
    fun insert(livro: LivroEntity)

    @Query("SELECT * FROM Livro")
    fun getAllLivros(): List<LivroEntity>

    @Query("SELECT * FROM Livro WHERE status = :status")
    fun getLivrosPorStatus(status: String): List<LivroEntity>

    @Query("SELECT * FROM Livro WHERE id = :id")
    fun getLivroById(id: Int): LivroEntity

    @Update
    fun update(livro: LivroEntity)

    @Delete
    fun delete(livro: LivroEntity): Int
}