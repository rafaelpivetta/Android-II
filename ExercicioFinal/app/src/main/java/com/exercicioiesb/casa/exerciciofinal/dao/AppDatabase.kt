package com.exercicioiesb.casa.exerciciofinal.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario

@Database(entities = arrayOf(Usuario::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
}