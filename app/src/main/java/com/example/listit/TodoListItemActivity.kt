package com.example.listit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.TodoListItem
import com.example.listit.databinding.ActivityTodoListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_todo_list_item.*

class TodoListItemActivity : AppCompatActivity() {

    private var TAG: String = "listit.TodoListItemActivity.kt"
    private lateinit var binding: ActivityTodoListItemBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    private var toDoItemOverview: MutableList<TodoListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTodoListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoListItemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.todoListItemRecyclerView.adapter = TodoRecyclerAdapter(toDoItemOverview, this::onDeleteTodoClicked)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val currentListTitle = intent.getStringExtra("TITLE")

        reference = firebaseDatabase.child("/users")
            .child(user.uid)
            .child("/lists")
            .child(currentListTitle.toString())
            .child("/todos")

        setSupportActionBar(toDoListItemToolbar)
        supportActionBar?.let { t ->
            t.setDisplayHomeAsUpEnabled(true)
            t.setDisplayShowHomeEnabled(true)
        }

        todoListTitle.text = intent.getStringExtra("TITLE")

        binding.addTodoButton.setOnClickListener {
            addNewToDo()
        }

        getDataFromFirebase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todolistitem_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteAllTodosActionMenu -> {
                reference.removeValue()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun addNewToDo() {
        val todoListItemId = reference.push().key
        val newTodoTitle = addNewTodoInput.text.toString().trim()
        val todoListItem = TodoListItem(newTodoTitle, false)

        when {
            newTodoTitle.isEmpty() -> Toast.makeText(this, "Enter a todo", Toast.LENGTH_SHORT).show()
            todoListItemId == null -> Toast.makeText(this, "Id does not exists", Toast.LENGTH_SHORT).show()
            else -> {
                reference.child(newTodoTitle).setValue(todoListItem)
                addNewTodoInput.text.clear()
            }
        }

    }

    private fun onDeleteTodoClicked(todo: TodoListItem){
        reference.child(todo.title).removeValue()
        todoListItemRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun getDataFromFirebase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val todoListItems = toDoItemOverview
                val adapter = binding.todoListItemRecyclerView.adapter
                todoListItems.clear()
                for (data in snapshot.children) {
                    val toDoListItem = data.getValue(TodoListItem::class.java)
                    todoListItemRecyclerView.adapter = adapter
                    if (toDoListItem != null) {
                        todoListItems.add(toDoListItem)
                    }
                }
            }
        })
    }
}