package com.example.listit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.data.ToDo
import com.example.listit.data.ToDoRecyclerAdapter
import com.example.listit.databinding.FragmentToDoOverviewBinding


class ToDoOverviewFragment : Fragment() {

    private val TAG:String = "listit.ToDoOverviewFragment"
    private var _binding: FragmentToDoOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var toDoRecyclerAdapter : ToDoRecyclerAdapter

    private var toDoCollection:MutableList<ToDo> =  mutableListOf()

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoOverviewBinding.inflate(layoutInflater)
        val view = binding.root

        binding.toDoItemsRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoRecyclerAdapter = ToDoRecyclerAdapter(toDoCollection)
        binding.toDoItemsRecyclerView.adapter = toDoRecyclerAdapter

        return view
    }
}