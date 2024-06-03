package com.example.messengerapp

class User{
    var email: String? = null
    var uid: String? = null
    var uName: String? = null

    constructor(){}
    constructor(uName: String, email: String, uid: String) : this() {
        this.uName = uName
        this.email = email
        this.uid = uid
    }
}
