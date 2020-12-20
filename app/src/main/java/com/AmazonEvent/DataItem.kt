package com.AmazonEvent


class DataItem {
    var id: String? = null
    var nome: String? = null
    var categoria: String? = null
    var local: String? = null
    var endereco: String? = null
    var data: String? = null
    var hora: String? = null
    var itemImage: String? = null
    var descricao: String? = null
    var dono: String? = null

    constructor() {}
    constructor(nome: String?, categoria: String?, local: String?, endereco: String?, data: String?, hora: String?, descricao: String?, itemImage: String?, id: String?, dono: String?) {
        this.nome = nome
        this.categoria = categoria
        this.local = local
        this.endereco = endereco
        this.data = data
        this.hora = hora
        this.descricao = descricao
        this.itemImage = itemImage
        this.id = id
        this.dono = dono
    }

    constructor(nome: String?, categoria: String?, local: String?, endereco: String?, data: String?, hora: String?, descricao: String?, itemImage: String?, id: String?) {
        this.nome = nome
        this.categoria = categoria
        this.local = local
        this.endereco = endereco
        this.data = data
        this.hora = hora
        this.descricao = descricao
        this.itemImage = itemImage
        this.id = id
    }

    constructor(nome: String?, categoria: String?, local: String?, endereco: String?, data: String?, hora: String?, descricao: String?, itemImage: String?) {
        this.nome = nome
        this.categoria = categoria
        this.local = local
        this.endereco = endereco
        this.data = data
        this.hora = hora
        this.descricao = descricao
        this.itemImage = itemImage
    }
}
