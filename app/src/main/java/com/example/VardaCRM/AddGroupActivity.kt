package com.example.VardaCRM

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_group)

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val n = findViewById<EditText>(R.id.etName).text.toString()
            val c = findViewById<EditText>(R.id.etCoach).text.toString()
            val s = findViewById<EditText>(R.id.etSchedule).text.toString()

            if (n.isNotEmpty()) {
                val newGroup = Group(0, n, c, s) // ID 0 — сервер сам заменит его
                lifecycleScope.launch {
                    try {
                        RetrofitClient.api.addGroup(newGroup)
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@AddGroupActivity, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        val errorMsg = if (e.message?.contains("400") == true) "Группа уже существует!" else "Ошибка сохранения"
                        Toast.makeText(this@AddGroupActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }
}