package com.example.listit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.data.TodoList
import com.example.listit.data.TodoListItem
import com.example.listit.databinding.ToDoItemBinding
import kotlinx.android.synthetic.main.activity_todo_list_item.view.*

class TodoRecyclerAdapter(private val todos: MutableList<TodoListItem>,
                          private val onDeleteTodoClicked:(TodoListItem) -> Unit,
                          private val onCheckboxChecked:(TodoListItem) -> Unit) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ToDoItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(
            todoList: TodoListItem,
            onDeleteTodoClicked: (TodoListItem) -> Unit,
            onCheckboxChecked: (TodoListItem) -> Unit
        )
        {
            binding.todoTitle.text = todoList.title
            binding.todoCheckBox.isChecked = todoList.isDone
            binding.deleteTodoButton.setOnClickListener { onDeleteTodoClicked(todoList)}
            binding.todoCheckBox.setOnClickListener { onCheckboxChecked(todoList) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ToDoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position], onDeleteTodoClicked, onCheckboxChecked)
    }

    override fun getItemCount(): Int = todos.size
}