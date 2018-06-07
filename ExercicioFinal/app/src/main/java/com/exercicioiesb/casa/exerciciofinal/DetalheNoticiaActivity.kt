package com.exercicioiesb.casa.exerciciofinal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detalhe_noticia.*

class DetalheNoticiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_noticia)

        imgFechar.setOnClickListener {
            finish()
        }

    }
}