package com.practicecoding.notetakingapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.practicecoding.notetakingapp.databinding.FragmentHidehomeBinding
import com.practicecoding.notetakingapp.databinding.FragmentHomeBinding
import com.practicecoding.notetakingapp.model.HideAdapter
import com.practicecoding.notetakingapp.model.HideNoteviewmodel
import com.practicecoding.notetakingapp.model.Noteadapter
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide

class Hidehome : Fragment(R.layout.fragment_hidehome), SearchView.OnQueryTextListener {
    private var longmenu: Menu? = null
    private var _binding: FragmentHidehomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var hidenotesViewmodel: HideNoteviewmodel
    private lateinit var noteadapter: HideAdapter
    lateinit var currentNotes: Note
    lateinit var notecurrent: NoteHide
    private val args: HidehomeArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHidehomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hidenotesViewmodel = (activity as MainActivity).hideNoteviewmodel

        setUpRecyclerView()
        if (args.notes1 != null) {
            currentNotes = args.notes1!!
            val title = currentNotes.noteTitle
            val body = currentNotes.noteBody
            val amount = currentNotes.noteAmount
            val time = currentNotes.timeStamp
            val hidenotes = NoteHide(0, title, body, amount, time)
            hidenotesViewmodel.addhideNote(hidenotes)
        }
        binding.fabbtn.setOnClickListener() {
            it.findNavController().navigate(R.id.action_hidehome_to_hidenewfragment)
        }

    }

    private fun setUpRecyclerView() {
        noteadapter = HideAdapter() { show -> showdeletemenu(show) }
        binding.recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteadapter
        }
        activity?.let {
            hidenotesViewmodel.getallhideNotes().observe(
                viewLifecycleOwner, { note ->
                    noteadapter.hidediffer.submitList(note)
                    updateUI(note)
                }
            )
        }

    }

    private fun updateUI(note: List<NoteHide>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                binding.cardView.visibility = View.GONE
                binding.recyclerview.visibility = View.VISIBLE

            } else {
                binding.cardView.visibility = View.VISIBLE
                binding.recyclerview.visibility = View.GONE

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        longmenu = menu
        inflater.inflate(R.menu.hidemenu, longmenu)
        showdeletemenu(false)
        val mMenuSearch = menu.findItem(R.id.hidemenu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)
    }

    fun showdeletemenu(show: Boolean) {
        longmenu?.findItem(R.id.hidemenu_long_delete)?.isVisible = show
        longmenu?.findItem(R.id.hidemenu_search)?.isVisible = !show
        longmenu?.findItem(R.id.unhidenote)?.isVisible = show
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hidemenu_long_delete -> {
                delete()
            }

            R.id.unhidenote -> {
                notecurrent = noteadapter.updatenote()

                val goto = HidehomeDirections.actionHidehomeToHomeFragment(notecurrent)
                hidenotesViewmodel.deletehideNote(notecurrent)
                setUpRecyclerView()
                findNavController().navigate(goto)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you Sure you want to delete this Note?")
            setPositiveButton("Delete") { _, _ ->
                notecurrent = noteadapter.updatenote()

                hidenotesViewmodel.deletehideNote(notecurrent)
                setUpRecyclerView()
                showdeletemenu(false)

            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
//        searchNote(query)
        return false
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    private fun searchNote(query: String) {
        val searchquery = "%$query"
        hidenotesViewmodel.searchhideNote(searchquery).observe(this,
            { list ->
                noteadapter.hidediffer.submitList(list)
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}


