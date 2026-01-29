package com.example.VardaCRM

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class GroupDetailsActivity : AppCompatActivity() {
    private lateinit var group: Group
    private lateinit var rv: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        // Получаем объект группы из Intent
        val receivedGroup = intent.getSerializableExtra("GROUP_OBJECT") as? Group
        if (receivedGroup == null) {
            finish()
            return
        }
        group = receivedGroup

        findViewById<TextView>(R.id.tvDetailTitle).text = group.name

        rv = findViewById(R.id.studentsRecyclerView)
        rv.layoutManager = LinearLayoutManager(this)

        // Настраиваем адаптер
        adapter = StudentAdapter(group.students) { student ->
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("STUDENT_OBJECT", student)
            intent.putExtra("G_ID", group.id) // Передаем ID группы для обновлений
            startActivity(intent)
        }
        rv.adapter = adapter

        findViewById<Button>(R.id.btnEditGroup).setOnClickListener {
            val intent = Intent(this, EditGroupActivity::class.java)
            intent.putExtra("GROUP_OBJECT", group)
            startActivity(intent)
        }

        val btnDeleteGroup = findViewById<Button>(R.id.btnDeleteGroup)

        btnDeleteGroup.setOnClickListener {
            // Рекомендуется добавить подтверждение удаления
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Удаление группы")
                .setMessage("Вы уверены, что хотите удалить группу ${group.name}?")
                .setPositiveButton("Да") { _, _ ->
                    deleteGroupFromServer(group.id)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        // При возвращении на экран лучше обновить данные с сервера
        loadLatestStudents()
    }

    private fun loadLatestStudents() {
        lifecycleScope.launch {
            try {
                // В ApiService должен быть метод getGroups, найдем нашу группу там
                val groups = RetrofitClient.api.getGroups()
                val updatedGroup = groups.find { it.id == group.id }
                updatedGroup?.let {
                    group = it
                    adapter.updateData(it.students)
                }
            } catch (e: Exception) {
                // Если нет сети, работаем с тем, что есть
            }
        }
    }

    private fun deleteGroupFromServer(groupId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.api.deleteGroup(groupId) //
                Toast.makeText(this@GroupDetailsActivity, "Группа удалена", Toast.LENGTH_SHORT).show()
                finish() // Возвращаемся на главный экран
            } catch (e: Exception) {
                Toast.makeText(this@GroupDetailsActivity, "Ошибка при удалении: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}