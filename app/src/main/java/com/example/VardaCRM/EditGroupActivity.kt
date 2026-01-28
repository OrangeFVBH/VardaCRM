package com.example.VardaCRM

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast // Нужно добавить
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Нужно добавить
import kotlinx.coroutines.launch // Нужно добавить

class EditGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_group)

        // Получаем объект группы целиком из Intent (так надежнее, чем искать в пустом DataManager)
        val group = intent.getSerializableExtra("GROUP_OBJECT") as? Group ?: return

        val etN = findViewById<EditText>(R.id.etName).apply { setText(group.name) }
        val etC = findViewById<EditText>(R.id.etCoach).apply { setText(group.coach) }
        val etS = findViewById<EditText>(R.id.etSchedule).apply { setText(group.schedule) }

        // Показываем блок добавления студента (только в режиме редактирования)
        findViewById<LinearLayout>(R.id.layoutAddStudent).visibility = View.VISIBLE

        // Кнопка добавления студента
        findViewById<Button>(R.id.btnAddStudentAction).setOnClickListener {
            val sN = findViewById<EditText>(R.id.etStudentName).text.toString()
            val sP = findViewById<EditText>(R.id.etStudentPhone).text.toString()

            if (sN.isNotEmpty()) {
                val newStudent = Student(0, sN, sP, "", "2025-01-01")

                // Запускаем корутину для сетевого запроса
                lifecycleScope.launch {
                    try {
                        RetrofitClient.api.addStudent(group.id, newStudent)
                        Toast.makeText(this@EditGroupActivity, "Ученик $sN добавлен!", Toast.LENGTH_SHORT).show()

                        // Очищаем поля после успеха
                        findViewById<EditText>(R.id.etStudentName).text.clear()
                        findViewById<EditText>(R.id.etStudentPhone).text.clear()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditGroupActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Введите имя ученика", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка сохранения изменений группы
        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            // В идеале тут тоже должен быть запрос RetrofitClient.api.updateGroup(...)
            // Но пока просто закроем экран, так как базовое добавление мы уже настроили
            finish()
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }
}