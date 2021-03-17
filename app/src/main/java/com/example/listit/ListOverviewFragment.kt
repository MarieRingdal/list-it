package com.example.listit

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listit.databinding.FragmentListOverviewBinding
import com.example.listit.data.List
import com.example.listit.data.ListOverviewRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_list_overview.view.*

class ListOverviewFragment : Fragment() {

    private val TAG:String = "listit.ListOverviewFragment"
    private var _binding:FragmentListOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter:ListOverviewRecyclerAdapter

    private var listOverview:MutableList<List> =  mutableListOf()

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

        view.addNewListButton.setOnClickListener {
            addNewListDialog()
        }


        return view
    }

    private fun addNewListDialog(){
        val alertDialogBuilder = AlertDialog.Builder(context)
        val alertDialogView = layoutInflater.inflate(R.layout.add_new_list, null)
        val newListTitle = alertDialogView.findViewById<EditText>(R.id.newListTitleEditText)

        with(alertDialogBuilder){
            setTitle("Add a new list")
            setView(alertDialogView)

            setPositiveButton("Add"){_, _ ->
                val listTitle = newListTitle.text.toString()
                if (listTitle.isNotEmpty()){
                    val list = List(listTitle, 0)
                    listAdapter.addList(list)
                    Toast.makeText(activity, "$listTitle was added",Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User added a new list successfully")
                } else {
                    Toast.makeText(activity, "Give your list a name :)",Toast.LENGTH_SHORT).show()
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
