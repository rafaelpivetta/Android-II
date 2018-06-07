package com.exercicioiesb.casa.exerciciofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private var noticiasList = ArrayList<Noticia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val it = intent

        var email = it.getStringExtra("email")

        noticiasList.add(Noticia(1, "Notícia1", "Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1 Notícia1.", "14/05/2018"))
        noticiasList.add(Noticia(2, "Notícia2", "Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2 Notícia2.", "14/05/2018"))
        noticiasList.add(Noticia(3, "Notícia3", "Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3 Notícia3.", "14/05/2018"))
        noticiasList.add(Noticia(4, "Notícia4", "Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4 Notícia4.", "14/05/2018"))
        noticiasList.add(Noticia(5, "Notícia5", "Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5 Notícia5.", "14/05/2018"))
        noticiasList.add(Noticia(6, "Notícia6", "Notícia6 Notícia6Notícia6Notícia6Notícia6Notícia6Notícia6Notícia6Notícia6Notícia6Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6 Notícia6.", "14/05/2018"))
        noticiasList.add(Noticia(7, "Notícia7", "Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7 Notícia7.", "14/05/2018"))
        noticiasList.add(Noticia(8, "Notícia8", "Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8 Notícia8.", "14/05/2018"))

        var noticiasAdapter = NoticiasAdapter(this, noticiasList)
        lsvNotes.adapter = noticiasAdapter
        lsvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->

            startActivity(Intent(this@MainActivity, DetalheNoticiaActivity::class.java))

        }

        tvPerfil.setOnClickListener {
            val it = Intent(this@MainActivity, PerfilUsuarioActivity::class.java)
            it.putExtra("email", email)

            startActivity(it)

        }

    }

    inner class NoticiasAdapter : BaseAdapter {

        private var noticiasList = ArrayList<Noticia>()
        private var context: Context? = null

        constructor(context: Context, noticiasList: ArrayList<Noticia>) : super() {
            this.noticiasList = noticiasList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.cell_noticias, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.tvTitulo.text = noticiasList[position].titulo
            vh.tvConteudo.text = noticiasList[position].conteudo
            vh.tvData.text = noticiasList[position].data

            return view
        }

        override fun getItem(position: Int): Any {
            return noticiasList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return noticiasList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvTitulo: TextView
        val tvConteudo: TextView
        var tvData: TextView

        init {
            this.tvTitulo = view?.findViewById(R.id.txvTitulo) as TextView
            this.tvConteudo = view?.findViewById(R.id.txvConteudo) as TextView
            this.tvData = view?.findViewById(R.id.txvData) as TextView
        }
    }

}