package com.practicecoding.notetakingapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.practicecoding.notetakingapp.databinding.FragmentHomeBinding
import com.practicecoding.notetakingapp.model.Noteadapter
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {
    private var longmenu: Menu? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewmodel: Noteviewmodel
    private lateinit var noteadapter: Noteadapter
    lateinit var notecurrent: Note
    lateinit var fromhidenote: NoteHide
    private val args: HomeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val callback = object :OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                findNavController().popBackStack()
////onPause()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewmodel = (activity as MainActivity).noteViewmodel

        setUpRecyclerView()
        if (args.notes4 != null) {
            fromhidenote = args.notes4!!
            val title = fromhidenote.noteTitle
            val body = fromhidenote.noteBody
            val amount = fromhidenote.noteAmount
            val time = fromhidenote.timeStamp
            val hidenotes = Note(0, title, body, amount, time)
            notesViewmodel.addNote(hidenotes)
        }
        binding.fabbtn.setOnClickListener() {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }

    }

    private fun setUpRecyclerView() {
        noteadapter = Noteadapter() { show -> showdeletemenu(show) }
        binding.recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteadapter
        }
        activity?.let {
            notesViewmodel.getallNotes().observe(
                viewLifecycleOwner, { note ->
                    noteadapter.differ.submitList(note)
                    updateUI(note)
                }
            )
        }

    }

    private fun updateUI(note: List<Note>?) {
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
        inflater.inflate(R.menu.menu, longmenu)
        showdeletemenu(false)
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)
    }

    fun showdeletemenu(show: Boolean) {
        longmenu?.findItem(R.id.menu_long_delete)?.isVisible = show
        longmenu?.findItem(R.id.menu_search)?.isVisible = !show
        longmenu?.findItem(R.id.hidenote)?.isVisible = show
        longmenu?.findItem(R.id.hiddennotes)?.isVisible = !show
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_long_delete -> {
                delete()
            }

            R.id.hidenote -> {
                notecurrent = noteadapter.updatenote()

                val goto = HomeFragmentDirections.actionHomeFragmentToHidehome(notecurrent)
                notesViewmodel.deleteNote(notecurrent)
                setUpRecyclerView()
                findNavController().navigate(goto)
            }

            R.id.hiddennotes -> {
              dialogbox()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogbox() {
        val builder = AlertDialog.Builder(context)

        val inflater = requireActivity().layoutInflater
        val dialoglayout = inflater.inflate(R.layout.dialog_fragment,null)
        val password  = dialoglayout.findViewById<EditText>(R.id.username)
with(builder){
            setPositiveButton(R.string.Hide,
                DialogInterface.OnClickListener { dialog, id ->
                    // Sign in the user.
                    if(password.text.toString() == "PayNote")
                    {                findNavController().navigate(R.id.action_homeFragment_to_hidehome)
                    }
                    else {
                        Toast.makeText(context, "You Enter The Wrong Password", Toast.LENGTH_LONG)
                            .show()
                    }

                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    null
                })
        setView(dialoglayout)
show()}

    }

    private fun delete() {

        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you Sure you want to delete this Note?")
            setPositiveButton("Delete") { _, _ ->
                notecurrent = noteadapter.updatenote()
                notesViewmodel.deleteNote(notecurrent)
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
        notesViewmodel.searchNote(searchquery).observe(this,
            { list ->
                noteadapter.differ.submitList(list)
            })

    }


    //    override fun onPause() {
//        super.onPause()
//    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}