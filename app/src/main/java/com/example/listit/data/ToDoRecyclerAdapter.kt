package com.example.listit.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.databinding.ToDoItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.to_do_item.view.*
import kotlinx.android.synthetic.main.to_do_item.view.deleteListButton

class ToDoRecyclerAdapter(private val toDos: MutableList<ToDoListItem>) :
    RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoListViewHolder>(){

    private lateinit var auth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit var reference:DatabaseReference
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference


    inner class ToDoListViewHolder(private val binding: ToDoItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(toDoList: ToDoListItem){
            binding.toDoTitle.text = toDoList.title
            binding.toDoCheckBox.isChecked = toDoList.isChecked
        }

        init {
            auth = FirebaseAuth.getInstance()
            user = auth.currentUser
            reference = firebaseDatabase.child("/users").child(user.uid)
                .child("/lists")

            val deleteToDoButton = binding.root.deleteListButton
            deleteToDoButton.setOnClickListener {
                deleteListDialog()
            }
        }

        private fun deleteListDialog(){
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)

            with(alertDialogBuilder){
                setTitle("Delete")
                setMessage("Are you sure?")

                setPositiveButton("Delete"){dialog, _ ->
                    deleteToDo()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel"){dialog, _ ->
                    dialog.cancel()
                }

                create()
                show()
            }
        }

        private fun deleteToDo(){ // Does not remove item from database
            toDos.removeAt(adapterPosition)
            reference.child(toDos[adapterPosition].title).removeValue()
            notifyDataSetChanged()
            Toast.makeText(binding.root.context, "Deleted", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        return ToDoListViewHolder(ToDoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val currentTodoListItem = toDos[position]
        holder.bind(currentTodoListItem)

        holder.itemView.apply {
            toDoTitle.text = currentTodoListItem.title
            toDoCheckBox.isChecked = currentTodoListItem.isChecked
            toggleStrikeThroughToDo(toDoTitle, currentTodoListItem.isChecked)
            toDoCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThroughToDo(toDoTitle, isChecked)
                currentTodoListItem.isChecked = !currentTodoListItem.isChecked
            }
        }
    }

    override fun getItemCount(): Int = toDos.size

    private fun toggleStrikeThroughToDo(toDoTitle: TextView, isChecked: Boolean){
        if (isChecked) {
            toDoTitle.paintFlags = toDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            toDoTitle.paintFlags = toDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

//    fun deleteAllCheckedItems(){
//        toDos.removeAll { toDo ->
//            toDo.isChecked
//        }
//        notifyDataSetChanged()
//    }

}
