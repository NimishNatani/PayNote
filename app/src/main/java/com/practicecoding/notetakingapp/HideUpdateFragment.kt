package com.practicecoding.notetakingapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.practicecoding.notetakingapp.databinding.FragmentHideUpdateBinding
import com.practicecoding.notetakingapp.model.HideNoteviewmodel
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide
import java.text.SimpleDateFormat
import java.util.Date

class HideUpdateFragment : Fragment(R.layout.fragment_hide_update) {
    private var _binding: FragmentHideUpdateBinding?=null
    private val binding get() = _binding!!
    private lateinit var  notesViewmodel: HideNoteviewmodel
    private lateinit var currentNote: NoteHide
//Since Update notefragment contains arguments in nav_graph
    private val args:HideUpdateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHideUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewmodel = (activity as MainActivity).hideNoteviewmodel
        currentNote = args.notes!!
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)
        binding.txtnoteamount.setText(currentNote.noteAmount)

        //if the user update the note
        binding.fabDone.setOnClickListener(){
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()
            val amount = binding.txtnoteamount.text.toString().trim()
            val current  = SimpleDateFormat("dd MMM, YYYY - HH:mm")
            val formatter =current.format(Date())
//            val paynote:String =when(binding.txtnoteamount.text.toString().toInt()){
//                0->{"PayNote"}
//                else ->{"Notes"}
//           }
            if(title.isNotEmpty()){
                val note = NoteHide(currentNote.id,title,body
                    ,amount
                    ,formatter
                    //                 ,paynote
                )
                notesViewmodel.updatehideNote(note)
                view.findNavController().navigate(R.id.action_hideUpdateFragment_to_hidehome)
            }
            else {
                Toast.makeText(context,"Please enter note title", Toast.LENGTH_LONG).show()
            }
        }
        binding.add2.setOnClickListener(){
            if(binding.etNoteAmount.text.isNotEmpty()){
                add(binding.etNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount", Toast.LENGTH_LONG).show()

            }
        }
        binding.minus2.setOnClickListener(){
            if(binding.etNoteAmount.text.isNotEmpty()){
                sub(binding.etNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount", Toast.LENGTH_LONG).show()

            }
        }
    }



    private fun deletenote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you Sure you want to delete this Note?")
            setPositiveButton("Delete"){_,_,->
                notesViewmodel.deletehideNote(currentNote)
                findNavController()?.navigate(R.id.action_hideUpdateFragment_to_hidehome)

            }
            setNegativeButton("Cancel",null)
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete->{
                deletenote()
            }
            R.id.share->{
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    if(binding.txtnoteamount.text.toString().toInt()!=0){
                        if(binding.txtnoteamount.text.toString().toInt()>0)
                        { putExtra(
                            Intent.EXTRA_TEXT,"${binding.etNoteBodyUpdate.text}\n" +
                                "You have to pay " +
                                "${binding.txtnoteamount.text} rupees to me")}
                        else {
                            putExtra(
                                Intent.EXTRA_TEXT,"${binding.etNoteBodyUpdate.text}\n" +
                                    "I have to pay " +
                                    "${(binding.txtnoteamount.text.toString().toInt())*(-1)} rupees to You")
                        }}
                    else {
                        putExtra(Intent.EXTRA_TEXT,"${binding.etNoteBodyUpdate.text}")
                    }
                    type = "text/plain"

                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                startActivity(shareIntent)
            }
            R.id.notification->{

                val direction = HideUpdateFragmentDirections.
                actionHideUpdateFragmentToNotificationFragment(null,currentNote)
                findNavController().navigate(direction)

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun add(number: Int){
        AlertDialog.Builder(activity).apply {
            setTitle("Add")
            setMessage("Are you Sure you want to Add this amount?")
            setPositiveButton("Yes"){_,_,->
                binding.txtnoteamount.setText("${number+binding.txtnoteamount.text.toString().toInt()}")

            }
            setNegativeButton("No",null)
        }.create().show()
    }
    private fun sub(number: Int){
        AlertDialog.Builder(activity).apply {
            setTitle("Subtract")
            setMessage("Are you Sure you want to Subtract this amount?")
            setPositiveButton("Yes"){_,_,->
                binding.txtnoteamount.setText("${(-number)+binding.txtnoteamount.text.toString().toInt()}")

            }
            setNegativeButton("No",null)
        }.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}