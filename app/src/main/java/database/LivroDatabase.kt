package com.agrodiario.livraria.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agrodiario.livraria.dao.LivroDAO
import com.agrodiario.livraria.entity.LivroEntity

@Database(
    entities = [LivroEntity::class],
    version = 1
)
abstract class LivroDatabase : RoomDatabase() {

    abstract fun livroDAO(): LivroDAO

    companion object {

        private lateinit var instance: LivroDatabase

        private const val DATABASE_NAME = "livros_db"

        fun getDatabase(context: Context): LivroDatabase {

            if (!::instance.isInitialized) {

                synchronized(this) {

                    instance = Room.databaseBuilder(
                        context,
                        LivroDatabase::class.java,
                        DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()

                }
            }

            return instance
        }
    }
}