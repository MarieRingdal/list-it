package com.example.listit

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listit.todolists.*
import com.example.listit.databinding.ActivityTodoListBinding
import com.example.listit.todolists.data.TodoList
import com.google.android.gms.tasks.Task
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
    private lateinit var auth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit var reference:DatabaseReference
    private var firebaseDatabase = FirebaseDatabase.getInstance().getReference("/users")
    private val todoListOverview:MutableList<TodoList> =  mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window){
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Explode()
            exitTransition = Explode()
            exitTransition.duration = 2000
        }

        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        reference = firebaseDatabase.child(user.uid).child("/lists")

        setSupportActionBar(todoListToolbar)

        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.todoListRecyclerView.adapter = ListRecyclerAdapter(todoListOverview, this::onDeleteListClicked)

        collapsingToolbar.title = "LIST IT"
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        collapsingToolbar.setExpandedTitleColor(Color.WHITE)

        val addNewListButton: FloatingActionButton = findViewById(R.id.addNewListButton)
        addNewListButton.setOnClickListener {
            addNewListDialog()
        }
    }

    override fun onStart() {
        getDataFromFirebase()
        super.onStart()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todolist_menu, menu)
        val menuItem = menu?.findItem(R.id.dayNightModeSwitch)
        val themeSwitch:SwitchCompat = menuItem?.actionView as SwitchCompat

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
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
                val todoList = TodoList(todoListTitle, 0, 0)
                val todoListId = reference.push().key

                when {
                    todoListTitle.isEmpty() -> Toast.makeText(context, "Give your list a name :)", Toast.LENGTH_SHORT).show()
                    todoListOverview.contains(todoListTitle) -> Toast.makeText(context, "There is already a list with that name", Toast.LENGTH_SHORT).show()
                    todoListId == null -> Toast.makeText(context, "Id does not exists", Toast.LENGTH_SHORT).show()
                    todoListTitle.contains(".") -> Toast.makeText(context, "List name can not contain symbols ", Toast.LENGTH_SHORT).show()
                    todoListTitle.contains("#") -> Toast.makeText(context, "List name can not contain symbols ", Toast.LENGTH_SHORT).show()
                    todoListTitle.contains("$") -> Toast.makeText(context, "List name can not contain symbols ", Toast.LENGTH_SHORT).show()
                    todoListTitle.contains("[") -> Toast.makeText(context, "List name can not contain symbols ", Toast.LENGTH_SHORT).show()
                    todoListTitle.contains("]") -> Toast.makeText(context, "List name can not contain symbols ", Toast.LENGTH_SHORT).show()
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

    private fun onDeleteListClicked(list: TodoList, holder: RecyclerView.ViewHolder){
        val alertDialogBuilder = AlertDialog.Builder(binding.root.context)

        with(alertDialogBuilder){
            setTitle("Delete")
            setMessage("Are you sure?")

            setPositiveButton("Delete"){dialog, _ ->
                reference.child(list.title).removeValue().addOnCompleteListener {
                    reference.child(list.title).setValue(null)
                    todoListRecyclerView.adapter?.notifyItemRemoved(holder.adapterPosition)
                    todoListRecyclerView.adapter?.notifyDataSetChanged()
                }
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
                val adapter = binding.todoListRecyclerView.adapter
                todoListOverview.clear()
                for (data in snapshot.children){
                    val todoList = data.getValue(TodoList::class.java)
                    todoListRecyclerView.adapter = adapter
                    if (todoList != null) {
                        todoListOverview.add(todoList)
                    }
                }
            }
        })
    }
}
