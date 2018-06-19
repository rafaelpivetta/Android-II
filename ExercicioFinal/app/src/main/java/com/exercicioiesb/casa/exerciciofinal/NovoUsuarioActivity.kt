package com.exercicioiesb.casa.exerciciofinal

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.exercicioiesb.casa.exerciciofinal.dao.AppDatabase
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_novousuario.*
import org.jetbrains.anko.longToast

class NovoUsuarioActivity : AppCompatActivity(){

    var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novousuario)

        mAuth = FirebaseAuth.getInstance()

        btnCriarConta.setOnClickListener {

            var util : Util = Util()

            if(edtEmail.text.toString().isEmpty() || edtSenha.text.toString().isEmpty() || edtConfirmarSenha.text.toString().isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!util.comparaSenhas(edtSenha.text.toString(), edtConfirmarSenha.text.toString())){
                Toast.makeText(this, "Senhas são diferentes", Toast.LENGTH_LONG).show()
                edtSenha.setText("")
                edtConfirmarSenha.setText("")
                return@setOnClickListener
            }

            if(!util.senhaValida(edtSenha.text.toString())){
                Toast.makeText(this, "A senha deve conter 6 dígitos, e conter pelo menos um caractere maiúsculo, um caractere especial e um número", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!util.emailValido(edtEmail.text.toString())){
                Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var u : Usuario = Usuario()
            u.email = edtEmail.text.toString()
            u.senha = edtSenha.text.toString()

            mAuth?.let { m ->
                m.createUserWithEmailAndPassword(u.email,u.senha).addOnCompleteListener({ task ->
                    if(task.isSuccessful){
                        Log.i("Novousuario", mAuth?.currentUser?.uid)
                        Log.i("Novousuario", mAuth?.currentUser?.isEmailVerified.toString())

                        if(mAuth?.currentUser != null && mAuth?.currentUser?.isEmailVerified == false){
                            mAuth?.currentUser?.sendEmailVerification()
                        }

                        startActivity(Intent(this@NovoUsuarioActivity, LoginActivity::class.java))
                        finish()
                    }else if(task.exception is FirebaseAuthInvalidCredentialsException){
                        longToast("E-mail inválido, digite um novo email")
                    }else if(task.exception is FirebaseAuthUserCollisionException){
                        longToast("E-mail já cadastrado")
                    }
                })
            }
        }


        /*var db = Room.databaseBuilder(applicationContext,
        AppDatabase::class.java, "exerciciofinal").allowMainThreadQueries().build()

var temCadastrado = false
val lista:List<Usuario>? = db.usuarioDao().all
if(lista != null) {
    for (p in lista) {

        Log.i("Novousuario1: ", p.email+" "+p.senha+" "+p.matricula)
        if(p.email == u.email){
            temCadastrado = true
        }

//                    db.usuarioDao().delete(u)
    }
}

if(!temCadastrado) {
    db.usuarioDao().insert(u)
    Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_LONG).show()
    db.close()
    startActivity(Intent(this@NovoUsuarioActivity, LoginActivity::class.java))
    finish()
}else{
    Toast.makeText(this, "Existe usuário cadastrado com esse e-mail", Toast.LENGTH_LONG).show()
}*/


        //mOAuth?.createUserWithEmailAndPassword("","")?.addOnCompleteListener({})


        tvLogin.setOnClickListener {
            startActivity(Intent(this@NovoUsuarioActivity, LoginActivity::class.java))
        }
    }

}