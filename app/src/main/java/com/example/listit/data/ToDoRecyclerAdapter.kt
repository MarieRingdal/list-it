package com.example.listit.data

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.databinding.ToDoItemBinding
import kotlinx.android.synthetic.main.to_do_item.view.*

class ToDoRecyclerAdapter(private val toDos: MutableList<ToDoListItem>) :
    RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoListViewHolder>(){

    class ToDoListViewHolder(val binding: ToDoItemBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        return ToDoListViewHolder(ToDoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val currentTodo = toDos[position]

        holder.itemView.apply {
            toDoTitle.text = currentTodo.title
            toDoCheckBox.isChecked = currentTodo.isChecked
            toggleStrikeThroughToDo(toDoTitle, currentTodo.isChecked)
            toDoCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThroughToDo(toDoTitle, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int = toDos.size

    private fun toggleStrikeThroughToDo(toDoTitle: TextView, isChecked: Boolean){
        if (isChecked) {
            toDoTitle.paintFlags = toDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            toDoTitle.paintFlags = toDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addToDo(todo: ToDoListItem){
        toDos.add(todo)
        notifyItemInserted(toDos.size - 1)
    }

    fun deleteAllCheckedItems(){
        toDos.removeAll { toDo ->
            toDo.isChecked
        }
        notifyDataSetChanged()
    }
}
