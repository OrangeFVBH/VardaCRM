package com.example.VardaCRM

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue

class GroupAdapter(private var list: List<Group>, private val onClick: (Group) -> Unit) :
    RecyclerView.Adapter<GroupAdapter.VH>() {

    private var filtered = list

    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(
        LayoutInflater.from(p.context).inflate(R.layout.item_group, p, false)
    )

    override fun onBindViewHolder(h: VH, pos: Int) {
        val i = filtered[pos]
        h.itemView.findViewById<TextView>(R.id.tvGroupName).text = i.name
        h.itemView.findViewById<TextView>(R.id.tvGroupCoach).text = "Тренер: ${i.coach}"
        h.itemView.findViewById<TextView>(R.id.tvGroupSchedule).text = i.schedule
        h.itemView.setOnClickListener { onClick(i) }
    }

    override fun getItemCount() = filtered.size

    fun filter(q: String) {
        filtered = if (q.isEmpty()) list else list.filter { it.name.contains(q, true) }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Group>) {
        this.list = newList
        this.filtered = newList
        notifyDataSetChanged()
    }
}

class StudentAdapter(private var list: List<Student>, private val onClick: (Student) -> Unit) :
    RecyclerView.Adapter<StudentAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(p: ViewGroup, t: Int): VH {
        val tv = TextView(p.context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setPadding(32, 32, 32, 32)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return VH(tv)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val i = list[pos]
        (h.itemView as TextView).text = i.name
        h.itemView.setOnClickListener { onClick(i) }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<Student>) {
        this.list = newList
        notifyDataSetChanged()
    }
}