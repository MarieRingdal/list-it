package com.example.listit

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.List
import com.example.listit.data.ListRecyclerAdapter
import com.example.listit.databinding.ActivityMainBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var TAG:String = "Mainactivity.kt"
    private lateinit var binding: ActivityMainBinding
    private lateinit var listRecyclerAdapter:ListRecyclerAdapter

    val listOverview:MutableList<List> =  mutableListOf(
        List("Huskeliste", 50),
        List("Handleliste", 25),
        List("Pakkeliste", 80),
        List("Hundeleker", 58),
        List("Huskeliste", 50),
        List("Handleliste", 25),
        List("Pakkeliste", 80),
        List("Hundeleker", 58)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listsRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerAdapter = ListRecyclerAdapter(listOverview)
        binding.listsRecyclerView.adapter = listRecyclerAdapter

        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbar.title = "LIST IT"

        val addNewListButton: FloatingActionButton = findViewById(R.id.addNewListButton)
        addNewListButton.setOnClickListener {
            addNewListDialog()
        }

    }

    private fun addNewListDialog(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        val alertDialogView = layoutInflater.inflate(R.layout.add_new_list_dialog, null)
        val newListTitle = alertDialogView.findViewById<EditText>(R.id.newListTitleInput)

        with(alertDialogBuilder){
            setTitle("Add a new list")
            setView(alertDialogView)

            setPositiveButton("Add"){_, _ ->
                val listTitle = newListTitle.text.toString()
                if (listTitle.isNotEmpty()){
                    val list = List(listTitle, 0)
                    listRecyclerAdapter.addList(list)
                    Toast.makeText(context, "$listTitle was added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User added a new list successfully")
                } else{
                    Toast.makeText(context, "Give your list a name :)", Toast.LENGTH_SHORT).show()
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