package com.example.VardaCRM

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class EditGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_group)

        val gId = intent.getIntExtra("G_ID", -1)
        val group = DataManager.groups.find { it.id == gId } ?: return

        val etN = findViewById<EditText>(R.id.etName).apply { setText(group.name) }
        val etC = findViewById<EditText>(R.id.etCoach).apply { setText(group.coach) }
        val etS = findViewById<EditText>(R.id.etSchedule).apply { setText(group.schedule) }

        findViewById<LinearLayout>(R.id.layoutAddStudent).visibility = View.VISIBLE

        findViewById<Button>(R.id.btnAddStudentAction).setOnClickListener {
            val sN = findViewById<EditText>(R.id.etStudentName).text.toString()
            val sP = findViewById<EditText>(R.id.etStudentPhone).text.toString()
            if (sN.isNotEmpty()) {
                group.students.add(Student(DataManager.nextSId(), sN, "", sP))
                findViewById<EditText>(R.id.etStudentName).text.clear()
                findViewById<EditText>(R.id.etStudentPhone).text.clear()
            }
        }

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            group.name = etN.text.toString()
            group.coach = etC.text.toString()
            group.schedule = etS.text.toString()
            finish()
        }
        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }
}