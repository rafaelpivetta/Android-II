package com.exercicioiesb.casa.exerciciofinal

class Noticia {

    var id: Int? = null
    var titulo: String? = null
    var conteudo: String? = null
    var data: String? = null

    constructor(id: Int, titulo: String, conteudo: String, data: String) {
        this.id = id
        this.titulo = titulo
        this.conteudo = conteudo
        this.data = data
    }
}