package com.example.listit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.data.TodoListItem
import com.example.listit.databinding.ToDoItemBinding

class TodoRecyclerAdapter(private val todos: MutableList<TodoListItem>,
                          private val onDeleteTodoClicked:(TodoListItem) -> Unit) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ToDoItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(todoList: TodoListItem, onDeleteTodoClicked: (TodoListItem) -> Unit){
            binding.todoTitle.text = todoList.title
            binding.todoCheckBox.isChecked = todoList.isChecked
            binding.deleteTodoButton.setOnClickListener { onDeleteTodoClicked(todoList)}
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