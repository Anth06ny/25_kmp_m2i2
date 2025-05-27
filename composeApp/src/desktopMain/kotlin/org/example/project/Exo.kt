package org.example.project

import androidx.compose.material.Text
import kotlin.system.exitProcess

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


}



data class UserBean(var email: String = "", var password: String? = null) {
    var age: Int =10

    fun toto(){
        age = 5
    }
}