package com.practicecoding.notetakingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.practicecoding.notetakingapp.databinding.FragmentHomeBinding
import com.practicecoding.notetakingapp.model.Noteadapter
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Note


class HomeFragment : Fragment(R.layout.fragment_home) ,SearchView.OnQueryTextListener{

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var  notesViewmodel:Noteviewmodel
    private lateinit var noteadapter: Noteadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
_binding = FragmentHomeBinding.inflate(inflater,container,false)
    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewmodel = (activity as MainActivity).noteViewmodel

        setUpRecyclerView()

        binding.fabbtn.setOnClickListener(){
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }

    }

    private fun setUpRecyclerView() {
        noteadapter = Noteadapter()
        binding.recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteadapter
        }
        activity?.let { notesViewmodel.getallNotes().observe(
            viewLifecycleOwner,{
                note-> noteadapter.differ.submitList(note)
                updateUI(note)
            }
        ) }

    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if(note.isNotEmpty()){
                binding.cardView.visibility = View.GONE
                binding.recyclerview.visibility = View.VISIBLE

            }
            else{
                binding.cardView.visibility = View.VISIBLE
                binding.recyclerview.visibility = View.GONE

            }
        }
}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu,menu)
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)

    }



    override fun onQueryTextSubmit(query: String?): Boolean {
//        searchNote(query)
        return false
    }



    override fun onQueryTextChange(newText: String?): Boolean {

if(newText !=null){

    searchNote(newText)

}
        return true


    }

    private fun searchNote(query: String) {
        val searchquery  = "%$query"
        notesViewmodel.searchNote(searchquery).observe(this,
            {
                list -> noteadapter.differ.submitList(list)
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}