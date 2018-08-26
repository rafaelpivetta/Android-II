package recyclerview.android.vogella.com.recyclerview

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var lista: RecyclerView
    var dados: MutableList<Aluno> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val x = Aluno()
        x.nome = "Maria"
        x.matricula = "123"
        dados.add(x)

        val y = Aluno()
        y.nome = "Jose"
        y.matricula = "654"
        dados.add(y)

        val adpt = AdaptadorAluno(this)
        lista = findViewById(R.id.recyclerView)
        lista.itemAnimator = DefaultItemAnimator()
        lista.layoutManager = LinearLayoutManager(this)
        lista.adapter = adpt

    }

    inner class AdaptadorAluno(private val ctx: Context) : RecyclerView.Adapter<AlunoViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
            val v = LayoutInflater.from(ctx).inflate(R.layout.row_layout, parent, false)
            return AlunoViewHolder(v)
        }

        override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
            val x = dados[position]
            holder.txtNomeAluno.text = x.nome
            holder.txtMatriculaAluno.text = x.matricula
        }

        override fun getItemCount(): Int {
            return dados.size
        }

    }

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtNomeAluno: TextView
        var txtMatriculaAluno: TextView

        init{
            itemView.setOnClickListener {
                println("TEST")
            }
            txtNomeAluno = itemView.findViewById(R.id.txtNome)
            txtMatriculaAluno = itemView.findViewById(R.id.txtMatricula)
        }

    }
}
