package com.example.VardaCRM

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GroupAdapter
    private var allGroups = listOf<Group>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.groupsRecyclerView)
        adapter = GroupAdapter(emptyList()) { group ->
            val intent = Intent(this, GroupDetailsActivity::class.java)
            intent.putExtra("GROUP_OBJECT", group) // Передаем весь объект
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<FloatingActionButton>(R.id.btnAddGroup).setOnClickListener {
            startActivity(Intent(this, AddGroupActivity::class.java))
        }

        findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filter(q ?: "")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                allGroups = RetrofitClient.api.getGroups()
                adapter.updateData(allGroups)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Ошибка сервера: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}