package com.example.listit.data

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.databinding.ListItemBinding
import com.example.listit.databinding.ToDoItemBinding
import kotlinx.android.synthetic.main.to_do_item.view.*
import kotlin.collections.List

class ToDoRecyclerAdapter(private val toDos:MutableList<ToDo>) :
    RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoListViewHolder>(){

    class ToDoListViewHolder(val binding: ToDoItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(toDo: ToDo){
            binding.toDoTitle.text = toDo.title
            binding.toDoCheckBox.isChecked = toDo.isChecked
        }
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
            strikeThroughToDo(toDoTitle, currentTodo.isChecked)
        }

    }

    override fun getItemCount(): Int = toDos.size

    private fun strikeThroughToDo(toDoTitle: TextView, isChecked: Boolean){
        if (isChecked) {
            toDoTitle.paintFlags = toDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            toDoTitle.paintFlags = toDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addToDo(todo: ToDo){
        toDos.add(todo)
        notifyItemInserted(toDos.size - 1)
    }
}
