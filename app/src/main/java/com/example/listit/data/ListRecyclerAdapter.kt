package com.example.listit.data

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.ToDoListItemActivity
import com.example.listit.databinding.ListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_item.view.*

class ListRecyclerAdapter(private val toDoLists:MutableList<ToDoList>) :
    RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>(){

    private val TAG:String = "listit.ListOverviewRecyclerAdapter.kt"
    private lateinit var reference: DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    inner class ListViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

        val context = binding.root.context

        fun bind(list: ToDoList){
            binding.listTitle.text = list.title
        }

        init {
            auth = FirebaseAuth.getInstance()
            user = auth.currentUser
            reference = firebaseDatabase.child("/users").child(user.uid)
                .child("/lists")

            itemView.setOnClickListener {
                val listTitle = binding.root.listTitle
                val intent = Intent(context, ToDoListItemActivity::class.java).apply {
                    putExtra("TITLE", listTitle.text)
                }
                context.startActivity(intent)
            }

            val deleteListButton = binding.root.deleteListButton
            deleteListButton.setOnClickListener {
                deleteListDialog()
            }
        }

        private fun deleteListDialog(){
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)

            with(alertDialogBuilder){
                setTitle("Delete")
                setMessage("Are you sure?")

                setPositiveButton("Delete"){dialog, _ ->
                    deleteList()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel"){dialog, _ ->
                    dialog.cancel()
                }

                create()
                show()
            }
        }

        private fun deleteList(){
            reference.child(toDoLists[adapterPosition].title).removeValue()
            toDoLists.removeAt(adapterPosition)
            notifyDataSetChanged()
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = toDoLists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentToDoList = toDoLists[position]
        holder.bind(currentToDoList)
    }

}
