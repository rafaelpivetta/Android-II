package com.exercicioiesb.casa.exerciciofinal

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.exercicioiesb.casa.exerciciofinal.dao.AppDatabase
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_redefinirsenha.*
import org.jetbrains.anko.longToast

class RedefinirSenhaActivity : AppCompatActivity(){

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefinirsenha)

        mAuth = FirebaseAuth.getInstance()

        btnRedefinir.setOnClickListener {

            var u : Usuario = Usuario()
            u.email = edtEmail.text.toString()
//            u.senha = edtSenha.text.toString()
//
//            if(u.email.isEmpty() || u.senha.isEmpty() || edtConfirmarSenha.text.toString().isEmpty()){
//                Log.i("Redefinirsenha:", "Preencha todos os campos")
//                return@setOnClickListener
//            }

            var util : Util = Util()
            if(!util.emailValido(u.email)){
                Log.i("Redefinirsenha:", "Email inválido")
                return@setOnClickListener
            }

            mAuth?.let { m ->
                m.sendPasswordResetEmail(u.email).addOnCompleteListener({ task ->
                    if(task.isSuccessful){
                        longToast("Para redefinir a senha verifique seu e-mail")
                    }else if(task.exception is FirebaseAuthInvalidUserException){
                        longToast("Não existe usuário cadastrado com esse e-mail")
                        Log.i("Redefinirsenha", task.exception.toString())
                    }
                })
            }

//            var db = Room.databaseBuilder(applicationContext,
//                    AppDatabase::class.java, "exerciciofinal").allowMainThreadQueries().build()

//            Log.i("Redefinirsenha: EMAIL->", u.email+" "+u.senha)
//            val usuario: Usuario = db.usuarioDao().findUserByEmailAndPass(u.email, u.senha)
//            if(usuario != null) {//Rever
//                Log.i("Redefinirsenha: ", usuario.email)
//                startActivity(Intent(this@LoginActivity, PerfilUsuarioActivity::class.java))
//            }else{
//                Log.i("Redefinirsenha: email->", "Verifique o email ou a senha")
//            }

        }
    }
}