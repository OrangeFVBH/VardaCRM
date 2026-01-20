package com.example.VardaCRM

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_group)

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val n = findViewById<EditText>(R.id.etName).text.toString()
            val c = findViewById<EditText>(R.id.etCoach).text.toString()
            val s = findViewById<EditText>(R.id.etSchedule).text.toString()
            if (n.isNotEmpty()) {
                DataManager.addGroup(n, c, s)
                finish()
            }
        }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }
}