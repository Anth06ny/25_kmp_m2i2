package org.example.project

fun main() {

    var user = UserBean("blabal", "hello")
    user.age = 30
    user.age++
    println(user)

    var user2 = UserBean("blabal", "hello")
    println(user2)

    val user3 = user2.copy(email = "aaaa")

    println(user == user2)
    println(user === user2)

    hello{ it.uppercase()}
}

var toto : (String)->String = {it ->
    println("cojucojez $it")
    "coucou $it"
}

fun hello(text : String = "", action : (String)->String) : String{
    action("coucou")
    return action("fzfzf")
}

fun String?.myIsNullOrBlank(): Boolean {
    return this == null || this.isBlank()
}


data class UserBean(var email: String = "", var password: String? = null) {
    var age: Int =10

    fun toto(){
        age = 5
    }
}