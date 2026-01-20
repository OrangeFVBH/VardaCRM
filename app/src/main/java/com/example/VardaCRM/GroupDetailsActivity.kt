package com.example.VardaCRM

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupDetailsActivity : AppCompatActivity() {
    private var gId: Int = -1
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        gId = intent.getIntExtra("G_ID", -1)
        val group = DataManager.groups.find { it.id == gId } ?: return

        findViewById<TextView>(R.id.tvDetailTitle).text = group.name
        rv = findViewById(R.id.studentsRecyclerView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = StudentAdapter(group.students) { student ->
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("G_ID", gId)
            intent.putExtra("S_ID", student.id)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnEditGroup).setOnClickListener {
            val intent = Intent(this, EditGroupActivity::class.java)
            intent.putExtra("G_ID", gId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val group = DataManager.groups.find { it.id == gId }
        (rv.adapter as? StudentAdapter)?.updateData(group?.students ?: mutableListOf())
    }
}