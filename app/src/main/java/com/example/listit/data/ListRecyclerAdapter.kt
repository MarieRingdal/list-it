package com.example.listit.data

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.R
import com.example.listit.ToDoListItemActivity
import com.example.listit.databinding.ListItemBinding
import kotlinx.android.synthetic.main.list_item.view.*

class ListRecyclerAdapter(private val lists:MutableList<ToDoList>) :
    RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>(){

    private val TAG:String = "listit.ListOverviewRecyclerAdapter.kt"

    inner class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(list: ToDoList){
            binding.listTitle.text = list.title
        }

        init {
            val listTitle = binding.root.listTitle

            itemView.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ToDoListItemActivity::class.java).apply {
                    putExtra("TITLE", listTitle.text)
                }
                context.startActivity(intent)
            }

            val deleteListButton = binding.root.findViewById<ImageButton>(R.id.deleteListButton)
            deleteListButton.setOnClickListener {
                deleteListDialog()
                Log.d(TAG, "Delete list button pressed")
            }
        }

        private fun deleteListDialog(){
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)

            with(alertDialogBuilder){
                setTitle("Delete")
                setMessage("Are you sure?")

                setPositiveButton("Delete"){dialog, _ ->
                    lists.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Deleted ", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel"){dialog, _ ->
                    dialog.cancel()
                }

                create()
                show()
            }
        }
    }

    override fun getItemCount(): Int = lists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        return ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentList = lists[position]
        holder.bind(currentList)

    }

    fun addList(list: ToDoList){
        lists.add(list)
        notifyItemInserted(lists.size - 1)
    }
    }