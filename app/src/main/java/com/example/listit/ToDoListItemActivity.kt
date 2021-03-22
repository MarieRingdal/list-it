package com.example.listit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.ToDoListItem
import com.example.listit.data.ToDoRecyclerAdapter
import com.example.listit.databinding.ActivityToDoListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_to_do_list_item.*
import kotlinx.android.synthetic.main.list_item.*

class ToDoListItemActivity : AppCompatActivity() {

    private var TAG: String = "listit.ToDoListItemActivity.kt"
    private lateinit var binding: ActivityToDoListItemBinding
    private lateinit var toDoRecyclerAdapter: ToDoRecyclerAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference
    val toDoItemOverview: MutableList<ToDoListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val currentListTitle = intent.getStringExtra("TITLE")

        reference = firebaseDatabase.child("/users").child(user.uid)
            .child("/lists")
            .child(currentListTitle.toString())
            .child("/todos")

        binding.toDosRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerAdapter = ToDoRecyclerAdapter(toDoItemOverview)
        binding.toDosRecyclerView.adapter = toDoRecyclerAdapter

        setSupportActionBar(toDoListItemToolbar)
        supportActionBar?.let { t ->
            t.setDisplayHomeAsUpEnabled(true)
            t.setDisplayShowHomeEnabled(true)
        }

        toDoListTitle.text = intent.getStringExtra("TITLE")

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
            R.id.deleteToDoActionMenu -> {
//                toDoRecyclerAdapter.deleteAllCheckedItems()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun addNewToDo() {
        val toDoListItemId = reference.push().key
        val newToDoTitle = addNewTodoInput.text.toString().trim()
        val toDoListItem = ToDoListItem(newToDoTitle, false)

        when {
            newToDoTitle.isEmpty() -> Toast.makeText(this, "Enter a todo", Toast.LENGTH_SHORT).show()
            toDoListItemId == null -> Toast.makeText(this, "Id does not exists", Toast.LENGTH_SHORT)
                .show()
            else -> {
                reference.child(newToDoTitle).setValue(toDoListItem)
                addNewTodoInput.text.clear()
            }
        }

    }

    private fun getDataFromFirebase() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val toDoListItems = toDoItemOverview
                toDoListItems.clear()
                for (data in snapshot.children) {
                    val toDoListItem = data.getValue(ToDoListItem::class.java)
                    if (toDoListItem != null) {
                        toDoListItems.add(toDoListItem)
                    }
                }
                if (toDoListItems.size > 0) {
                    val adapter = ToDoRecyclerAdapter(toDoListItems)
                    toDosRecyclerView.adapter = adapter
                }
            }
        })
    }
}