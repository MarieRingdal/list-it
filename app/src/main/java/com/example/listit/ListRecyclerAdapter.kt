package com.example.listit

import android.content.Context
import android.content.Intent 
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.data.TodoList
import com.example.listit.databinding.ListItemBinding

class ListRecyclerAdapter(private val todoLists:MutableList<TodoList>,
                          private val onDeleteListClicked:(TodoList) -> Unit) :
    RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>(){

    inner class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

        val context:Context = binding.root.context

        fun bind(list: TodoList, onDeleteListClicked:(TodoList) -> Unit){
            binding.listTitle.text = list.title
            binding.listProgressBar.progress = list.checkedItems
            binding.listProgressBar.max = list.totalItems
            binding.deleteListButton.setOnClickListener { onDeleteListClicked(list) }
        }

        init {
            itemView.setOnClickListener {
                val listTitle = binding.listTitle
                val intent = Intent(context, TodoListItemActivity::class.java).apply {
                    putExtra("TITLE", listTitle.text)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(todoLists[position], onDeleteListClicked)
    }

    override fun getItemCount(): Int = todoLists.size
}
