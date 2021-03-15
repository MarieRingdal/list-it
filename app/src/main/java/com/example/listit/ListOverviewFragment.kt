package com.example.listit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.databinding.FragmentListOverviewBinding
import com.example.listit.data.List
import com.example.listit.data.ListOverviewRecyclerAdapter

class ListOverviewFragment : Fragment() {

    private var _binding:FragmentListOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter:ListOverviewRecyclerAdapter

    private var listOverview:MutableList<List> = mutableListOf(
        List("Huskeliste", 50),
        List("Handleliste", 25),
        List("Pakkeliste", 80),
        List("Hundeleker", 58)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListOverviewBinding.inflate(layoutInflater)
        val view = binding.root

        binding.listsRecyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = ListOverviewRecyclerAdapter(listOverview)
        binding.listsRecyclerView.adapter = listAdapter

        return view
    }

    // TODO: Add new list function

    // TODO: Add delete list function


}