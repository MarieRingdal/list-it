package com.example.listit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.ToDoListItem
import com.example.listit.data.ToDoRecyclerAdapter
import com.example.listit.databinding.ActivityToDoBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_to_do.*


class ToDoActivity : AppCompatActivity() {

    private var TAG:String = "ToDoActivity.kt"
    private lateinit var binding: ActivityToDoBinding
    private lateinit var toDoRecyclerAdapter: ToDoRecyclerAdapter
    var database = FirebaseDatabase.getInstance().reference

    val toDoOverview:MutableList<ToDoListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toDosRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerAdapter = ToDoRecyclerAdapter(toDoOverview)
        binding.toDosRecyclerView.adapter = toDoRecyclerAdapter

        toDoListTitle.text = intent.getStringExtra("TITLE")


        val addNewToDoButton:Button = binding.addTodoButton

        addNewToDoButton.setOnClickListener {
            val toDoTitle = addNewTodoInput.text.toString()
            val toDoListItem = ToDoListItem(toDoTitle, 0)

            if (toDoTitle.isNotEmpty()){
                toDoRecyclerAdapter.addToDo(toDoListItem)
                database.child(toDoTitle).setValue(toDoListItem)
                addNewTodoInput.text.clear()
            } else {
                Toast.makeText(this, "Enter a todo", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.todoActionMenu -> {
                toDoRecyclerAdapter.deleteAllCheckedItems()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}