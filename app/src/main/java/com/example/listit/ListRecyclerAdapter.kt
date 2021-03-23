package com.example.listit

import android.content.Context
import android.content.Intent 
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.data.ToDoList
import com.example.listit.databinding.ListItemBinding
import kotlinx.android.synthetic.main.list_item.view.*

class ListRecyclerAdapter(private val toDoLists:MutableList<ToDoList>, 
                          private val onDeleteListClicked:(ToDoList) -> Unit) : 
    RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>(){

    inner class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

        val context:Context = binding.root.context

        fun bind(list: ToDoList, onDeleteListClicked:(ToDoList) -> Unit){
            binding.listTitle.text = list.title
            binding.deleteListButton.setOnClickListener { onDeleteListClicked(list) }
        }

        init {
            itemView.setOnClickListener {
                val listTitle = binding.root.listTitle
                val intent = Intent(context, ToDoListItemActivity::class.java).apply {
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
        holder.bind(toDoLists[position], onDeleteListClicked)
    }

    override fun getItemCount(): Int = toDoLists.size
}
