package com.example.listit.todolists

import android.content.Context
import android.content.Intent 
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.R
import com.example.listit.TodoListItemActivity
import com.example.listit.databinding.ListItemBinding
import com.example.listit.todolists.data.TodoList
import com.example.listit.todolists.data.TodoListItem

class ListRecyclerAdapter(private var todoLists:MutableList<TodoList>,
                          private val onDeleteListClicked:(TodoList, RecyclerView.ViewHolder) -> Unit,
                          private val onFavoriteClicked:(TodoList) -> Unit) :
    RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>(){

    inner class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

        val context:Context = binding.root.context

        fun bind(
            list: TodoList,
            onFavoriteClicked: (TodoList) -> Unit)
        {
            binding.listTitle.text = list.title
            binding.listProgressBar.progress = list.checkedItems
            binding.listProgressBar.max = list.totalItems
            binding.favoriteListButton.setOnClickListener { onFavoriteClicked(list) }
            if (list.isFavorite){
                binding.favoriteListButton.setImageResource(R.drawable.ic_favorite_true)
            }
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
        holder.bind(todoLists[position], onFavoriteClicked)
        holder.binding.deleteListButton.setOnClickListener { onDeleteListClicked(todoLists[position], holder) }
    }

    override fun getItemCount(): Int = todoLists.size

}
