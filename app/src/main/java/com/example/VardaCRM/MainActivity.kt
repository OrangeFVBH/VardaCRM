package com.example.VardaCRM

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.groupsRecyclerView)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAddGroup)
        val search = findViewById<SearchView>(R.id.searchView)

        adapter = GroupAdapter(DataManager.groups) { group ->
            val intent = Intent(this, GroupDetailsActivity::class.java)
            intent.putExtra("G_ID", group.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnAdd.setOnClickListener { startActivity(Intent(this, AddGroupActivity::class.java)) }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filter(q ?: "")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.updateData(DataManager.groups)
        }
    }
}