package com.example.listit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.databinding.ListItemBinding

class ListOverviewRecyclerAdapter(private val lists:MutableList<List>):
    RecyclerView.Adapter<ListOverviewRecyclerAdapter.ViewHolder>(){

    class ViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: List) {
            binding.listTitle.text = list.title
            binding.listProgression.progress = list.progression
        }

    }

    override fun getItemCount(): Int = lists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lists[position]
        holder.bind(list)
    }
}