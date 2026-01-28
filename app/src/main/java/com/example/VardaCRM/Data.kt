package com.example.VardaCRM
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Student(
    val id: Int,
    var name: String,
    var phoneNumber: String,
    var lastPaymentDate: String = "",
    var subEndDate: String = "2024-12-31",
    var photoUrl: String? = null
) : Serializable

fun Student.isDebtor(): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val endDate = sdf.parse(this.subEndDate)
        endDate?.before(Date()) ?: false
    } catch (e: Exception) { false }
}

data class Group(
    val id: Int,
    var name: String,
    var coach: String,
    var schedule: String,
    val students: MutableList<Student> = mutableListOf()
) : Serializable