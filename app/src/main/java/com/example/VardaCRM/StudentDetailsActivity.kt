package com.example.VardaCRM

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        val student = intent.getSerializableExtra("STUDENT_OBJECT") as? Student ?: return
        val groupId = intent.getIntExtra("G_ID", -1)

        findViewById<TextView>(R.id.tvStName).text = student.name
        findViewById<TextView>(R.id.tvStPhone).text = "Тел: ${student.phoneNumber}"

        val etPayment = findViewById<EditText>(R.id.etPaymentDate)
        val etSubEndDate = findViewById<EditText>(R.id.etSubEndDate)
        val btnSelectDate = findViewById<Button>(R.id.btnSelectDate)

        etPayment.setText(student.lastPaymentDate)
        etSubEndDate.setText(student.subEndDate)

        val calendar = Calendar.getInstance()

        // Кнопка выбора даты
        btnSelectDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                etSubEndDate.setText(selectedDate)
                student.subEndDate = selectedDate

                // Сразу сохраняем изменение на сервер (нужен метод в API)
                saveStudentChanges(groupId, student)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        // Логика удаления (нужен метод DELETE в API и Python)
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            Toast.makeText(this, "Функция удаления в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveStudentChanges(groupId: Int, student: Student) {
        lifecycleScope.launch {
            try {
                // В идеале тут RetrofitClient.api.updateStudent(groupId, student)
                Toast.makeText(this@StudentDetailsActivity, "Дата обновлена", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@StudentDetailsActivity, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
            }
        }
    }
}