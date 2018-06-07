package com.exercicioiesb.casa.exerciciofinal

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.exercicioiesb.casa.exerciciofinal.dao.AppDatabase
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        tvCriarConta.setOnClickListener {
            startActivity(Intent(this@LoginActivity, NovoUsuarioActivity::class.java))
        }

        tvRedefinirSenha.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RedefinirSenhaActivity::class.java))
        }

        btnLogin.setOnClickListener {

            var u : Usuario = Usuario()
            u.email = edtEmail.text.toString()
            u.senha = edtSenha.text.toString()

            if(u.email.isEmpty() || u.senha.isEmpty()){
                Toast.makeText(this, "Preencha o campo Email e/ou Senha", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var util : Util = Util()
            if(!util.emailValido(u.email)){
                Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            mAuth?.let { m ->

                m.signInWithEmailAndPassword(u.email, u.senha).addOnCompleteListener({ task ->

                    if(task.isSuccessful){

                        Log.i("New", mAuth?.currentUser?.uid)

                        val it = Intent(this@LoginActivity, MainActivity::class.java)
                        it.putExtra("email", u.email)
                        it.putExtra("senha", u.senha)
                        startActivity(it)
                        finish()
                    }else if(task.exception is FirebaseAuthInvalidCredentialsException){
                        Log.i("ERR", task.exception.toString()+" "+task.exception)
                        longToast("A senha está inválida ou o usuário não possui uma senha")
                        /*Toast.makeText(this, "Verifique o email ou a senha", Toast.LENGTH_LONG).show()
                        edtEmail.text = Editable.Factory.getInstance().newEditable("")*/
                    }else if(task.exception is FirebaseAuthInvalidUserException){
                        Log.i("ERR", task.exception.toString()+" "+task.exception)
                        longToast("Não existe usuário cadastrado com o e-mail informado")
                    }else{
                        Log.i("ERR", task.exception.toString()+" "+task.exception)
                        longToast("Verifique o e-mail e a senha")
                    }

                })

            }

            /*var db = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "exerciciofinal").allowMainThreadQueries().build()


            var usuario: Usuario = db.usuarioDao().findUserByEmailAndPass(u.email, u.senha)
            if(usuario != null) {//Rever, pois sempre retorna true?

                Log.i("Loginusuario: sucesso->", usuario.matricula)
                val it = Intent(this@LoginActivity, MainActivity::class.java)
                it.putExtra("email", u.email)
                it.putExtra("senha", u.senha)
                db.close()
                startActivity(it)
                finish()
            }else{
                Toast.makeText(this, "Verifique o email ou a senha", Toast.LENGTH_LONG).show()
                edtEmail.text = Editable.Factory.getInstance().newEditable("")
            }*/
        }
    }
}
