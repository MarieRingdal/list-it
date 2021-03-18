package com.example.listit

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.*
import com.example.listit.databinding.ActivityMainBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var TAG:String = "Mainactivity.kt"
    private lateinit var binding: ActivityMainBinding
    private lateinit var listRecyclerAdapter:ListRecyclerAdapter
    private lateinit var auth:FirebaseAuth
    var database = FirebaseDatabase.getInstance().reference

    val listOverview:MutableList<ToDoList> =  mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        signInAnonomusly()

        binding.listsRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerAdapter = ListRecyclerAdapter(listOverview)
        binding.listsRecyclerView.adapter = listRecyclerAdapter

        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbar.title = "LIST IT"

        val addNewListButton: FloatingActionButton = findViewById(R.id.addNewListButton)
        addNewListButton.setOnClickListener {
            addNewList()
        }

    }

    private fun signInAnonomusly(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login success ${it.user}")
        }.addOnFailureListener{
            Log.e(TAG, "Login failed", it)
        }
    }

    private fun addNewList(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertDialogView = layoutInflater.inflate(R.layout.add_new_list_dialog, null)
        val newListTitle = alertDialogView.findViewById<EditText>(R.id.newListTitleInput)

        with(alertDialogBuilder){
            setTitle("Add a new list")
            setView(alertDialogView)

            setPositiveButton("Add"){_, _ ->
                val toDoListTitle = newListTitle.text.toString()
                val toDoList = ToDoList(toDoListTitle, ToDoListItem("", 0, false))

                when {
                    toDoListTitle.isEmpty() -> Toast.makeText(context, "Give your list a name :)", Toast.LENGTH_SHORT).show()
                    listOverview.contains(toDoList) -> Toast.makeText(context, "There is already a list with that name", Toast.LENGTH_SHORT).show()
                else -> {
                    listRecyclerAdapter.addList(toDoList)
                    database.child(toDoListTitle).setValue(toDoList)
                    Toast.makeText(context, "$toDoListTitle was added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User added a new list successfully")
                    }
                }
            }
            setNegativeButton("Cancel"){dialog, _ ->
                dialog.cancel()
                Log.d(TAG, "User cancelled adding list")
            }

            create()
            show()
        }
    }
}