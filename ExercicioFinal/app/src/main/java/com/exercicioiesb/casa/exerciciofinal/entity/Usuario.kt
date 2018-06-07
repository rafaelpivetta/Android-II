package com.exercicioiesb.casa.exerciciofinal.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "usuario")
data class Usuario (
        @PrimaryKey(autoGenerate = true)
        var uid: Int = 0,

        @ColumnInfo(name = "nome") var nome: String = "",

        @ColumnInfo(name = "email") var email: String = "",

        @ColumnInfo(name = "matricula") var matricula: String = "",

        @ColumnInfo(name = "senha") var senha: String = "",

        @ColumnInfo(name = "telefone") var telefone: String = "",

        @ColumnInfo(name = "foto") var foto: String = ""

)