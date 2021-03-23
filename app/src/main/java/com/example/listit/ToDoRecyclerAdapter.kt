package com.example.listit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.data.ToDoListItem
import com.example.listit.databinding.ToDoItemBinding

class ToDoRecyclerAdapter(private val todos: MutableList<ToDoListItem>,
                          private val onDeleteTodoClicked:(ToDoListItem) -> Unit) :
    RecyclerView.Adapter<ToDoRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ToDoItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(todoList: ToDoListItem, onDeleteTodoClicked: (ToDoListItem) -> Unit){
            binding.toDoTitle.text = todoList.title
            binding.toDoCheckBox.isChecked = todoList.isChecked
            binding.deleteListButton.setOnClickListener { onDeleteTodoClicked(todoList)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ToDoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position], onDeleteTodoClicked)
    }

    override fun getItemCount(): Int = todos.size
}