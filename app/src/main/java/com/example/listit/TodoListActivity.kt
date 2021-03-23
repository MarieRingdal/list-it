package com.example.listit

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.*
import com.example.listit.databinding.ActivityTodoListBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_todo_list.*

class TodoListActivity : AppCompatActivity() {

    private var TAG:String = "listit.ToDoListActivity.kt"
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var listRecyclerAdapter: ListRecyclerAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit var reference:DatabaseReference
    private var firebaseDatabase = FirebaseDatabase.getInstance().getReference("/users")
    private val toDoListOverview:MutableList<TodoList> =  mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        reference = firebaseDatabase.child(user.uid).child("/lists")

        setSupportActionBar(todoListToolbar)

        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.todoListRecyclerView.adapter = ListRecyclerAdapter(toDoListOverview, this::onDeleteListClicked)

        getDataFromFirebase()

        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbar.title = "LIST IT"

        val addNewListButton: FloatingActionButton = findViewById(R.id.addNewListButton)
        addNewListButton.setOnClickListener {
            addNewListDialog()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todolist_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.signOutOption -> {
                signOutUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun addNewListDialog(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertDialogView = layoutInflater.inflate(R.layout.add_new_list_dialog, null)
        val newTodoListTitle = alertDialogView.findViewById<EditText>(R.id.newListTitleInput)

        with(alertDialogBuilder){
            setTitle("Add a new list")
            setView(alertDialogView)

            setPositiveButton("Add"){_, _ ->
                val todoListTitle = newTodoListTitle.text.toString().trim()
                val todoList = TodoList(todoListTitle)
                val todoListId = reference.push().key

                when {
                    todoListTitle.isEmpty() -> Toast.makeText(context, "Give your list a name :)", Toast.LENGTH_SHORT).show()
                    toDoListOverview.contains(todoListTitle) -> Toast.makeText(context, "There is already a list with that name", Toast.LENGTH_SHORT).show()
                    todoListId == null -> Toast.makeText(context, "Id does not exists", Toast.LENGTH_SHORT).show()
                    else -> {
                        reference.child(todoListTitle).setValue(todoList)
                        Toast.makeText(context, "$todoListTitle was added", Toast.LENGTH_SHORT).show()
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

    private fun onDeleteListClicked(list: TodoList){
        val alertDialogBuilder = AlertDialog.Builder(binding.root.context)

        with(alertDialogBuilder){
            setTitle("Delete")
            setMessage("Are you sure?")

            setPositiveButton("Delete"){dialog, _ ->
                reference.child(list.title).removeValue()
                dialog.dismiss()
            }
            setNegativeButton("Cancel"){dialog, _ ->
                dialog.cancel()
            }

            create()
            show()
        }
    }

    private fun signOutUser(){
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }


    private fun getDataFromFirebase(){
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val todoLists = toDoListOverview
                val adapter = binding.todoListRecyclerView.adapter
                todoLists.clear()
                for (data in snapshot.children){
                    val todoList = data.getValue(TodoList::class.java)
                    todoListRecyclerView.adapter = adapter
                    if (todoList != null) {
                        todoLists.add(todoList)
                    }
                }
            }
        })
    }
}
