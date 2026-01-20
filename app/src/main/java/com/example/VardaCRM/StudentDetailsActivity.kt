package com.example.VardaCRM

import android.text.Editable
import android.text.TextWatcher
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        val gId = intent.getIntExtra("G_ID", -1)
        val sId = intent.getIntExtra("S_ID", -1)
        val group = DataManager.groups.find { it.id == gId }
        val student = group?.students?.find { it.id == sId } ?: return

        findViewById<TextView>(R.id.tvStName).text = student.name
        findViewById<TextView>(R.id.tvStPhone).text = "Тел: ${student.phoneNumber}"
        val etPayment = findViewById<EditText>(R.id.etPaymentDate)
        etPayment.setText(student.lastPaymentDate)

        etPayment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { student.lastPaymentDate = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            group.students.remove(student)
            finish()
        }
    }
}