package com.exercicioiesb.casa.exerciciofinal

import android.util.Log
import java.util.regex.Pattern

class Util {

    fun emailValido(email: String): Boolean {
        val emailRegEx: String
        val pattern: Pattern

        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$"

        pattern = Pattern.compile(emailRegEx)
        val matcher = pattern.matcher(email)
        return if (!matcher.find()) {
            false
        } else true
    }

    fun senhaValida(senha: String): Boolean {

        if (senha.length < 6) {
            return false
        } else {
            var chUpperCase = false
            var chLowerCase = false
            var chDigito = false
            var chEspecial = false

            for (i in 0 until senha.length) {
                if (Character.isUpperCase(senha[i])) {
                    Log.i("Novousuario", "uppercase:"+senha[i].toString())
                    chUpperCase = true
                }
            }
            for (j in 0 until senha.length) {
                if (Character.isLowerCase(senha[j])) {
                    Log.i("Novousuario", "lowercase: "+senha[j].toString())
                    chLowerCase = true
                }
            }
            for (k in 0 until senha.length) {
                if (Character.isDigit(senha[k])) {
                    Log.i("Novousuario", "digit: "+senha[k].toString())
                    chDigito = true
                }
            }
            for (l in 0 until senha.length) {
                if (!Character.isDigit(senha[l]) && !Character.isLetter(senha[l])) {
                    Log.i("Novousuario", "special: "+senha[l].toString())
                    chEspecial = true
                }
            }
            if(chUpperCase && chLowerCase && chDigito && chEspecial)
                return true
            else
                return false
        }
    }

    fun comparaSenhas(senha: String, senhaConfirmada: String): Boolean{
        if(senha.equals(senhaConfirmada)){
            return true
        }else{
            return false
        }
    }

}