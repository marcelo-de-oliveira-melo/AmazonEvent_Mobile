package com.AmazonEvent

class User {
    var id = 0
    var email: String? = null
    var senha: String? = null

    constructor() {}
    constructor(email: String?) {
        this.email = email
    }

    constructor(id: Int, email: String?, senha: String?) {
        this.email = email
        this.senha = senha
        this.id = id
    }
}
