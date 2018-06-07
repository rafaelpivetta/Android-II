package com.exercicioiesb.casa.exerciciofinal.dao

import android.arch.persistence.room.*
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario

@Dao
interface UsuarioDAO {
    @get:Query("SELECT * FROM usuario")
    val all: List<Usuario>

    @Query("SELECT * FROM usuario WHERE uid IN (:uid)")
    fun loadAllById(uid: Int): Usuario

    @Query("SELECT * FROM usuario WHERE email = :email AND senha = :senha")
    fun findUserByEmailAndPass(email: String, senha: String): Usuario

    @Query("SELECT * FROM usuario WHERE email = :email")
    fun findUserByEmail(email: String): Usuario

    @Insert
    fun insert(usuario: Usuario)

    @Delete
    fun delete(usuario: Usuario)

    @Update
    fun update(usuario: Usuario)
}
