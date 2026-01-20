package com.example.VardaCRM
import java.io.Serializable

data class Student(val id: Int, var name: String, var lastPaymentDate: String, var phoneNumber: String) : Serializable
data class Group(val id: Int, var name: String, var coach: String, var schedule: String, val students: MutableList<Student> = mutableListOf()) : Serializable

object DataManager {
    val groups = mutableListOf<Group>()
    private var gId = 0
    private var sId = 0
    fun addGroup(n: String, c: String, s: String) { groups.add(Group(gId++, n, c, s)) }
    fun nextSId() = sId++
}