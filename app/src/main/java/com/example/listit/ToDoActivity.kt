package com.example.listit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.ToDo
import com.example.listit.data.ToDoRecyclerAdapter
import com.example.listit.databinding.ActivityToDoBinding
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.android.synthetic.main.to_do_item.*
import org.w3c.dom.Text

class ToDoActivity : AppCompatActivity() {

    private var TAG:String = "ToDoActivity.kt"
    private lateinit var binding: ActivityToDoBinding
    private lateinit var toDoRecyclerAdapter: ToDoRecyclerAdapter

    val toDoOverview:MutableList<ToDo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toDosRecyclerView.layoutManager = LinearLayoutManager(this)
        toDoRecyclerAdapter = ToDoRecyclerAdapter(toDoOverview)
        binding.toDosRecyclerView.adapter = toDoRecyclerAdapter

        toDoListTitle.text = intent.getStringExtra("TITLE")


        val addNewToDoButton: Button = findViewById(R.id.addTodoButton)
        addNewToDoButton.setOnClickListener {
            val toDoTitle = addNewTodoInput.text.toString()
            if (toDoTitle.isNotEmpty()){
                val toDo = ToDo(toDoTitle, false)
                toDoRecyclerAdapter.addToDo(toDo)
                addNewTodoInput.text.clear()
            } else {
                Toast.makeText(this, "Enter a todo!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}