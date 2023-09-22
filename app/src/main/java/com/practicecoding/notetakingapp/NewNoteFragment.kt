package com.practicecoding.notetakingapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.findNavController
import com.practicecoding.notetakingapp.databinding.FragmentHomeBinding
import com.practicecoding.notetakingapp.databinding.FragmentNewNoteBinding
import com.practicecoding.notetakingapp.model.Noteadapter
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Note
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date


class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var _binding: FragmentNewNoteBinding?=null
    private val binding get() = _binding!!
    private lateinit var  notesViewmodel: Noteviewmodel
    private lateinit var noteadapter: Noteadapter
    private lateinit var mView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewNoteBinding.inflate(inflater,container,false)
        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewmodel = (activity as MainActivity).noteViewmodel
        mView = view
        binding.add1.setOnClickListener(){
            if(binding.edtNoteAmount.text.isNotEmpty()){
                add(binding.edtNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount",Toast.LENGTH_LONG).show()

            }
        }
        binding.minus1.setOnClickListener(){
            if(binding.edtNoteAmount.text.isNotEmpty()){
                sub(binding.edtNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount",Toast.LENGTH_LONG).show()

            }
        }
    }




    private fun savenote(view: View){
        val noteTitle = binding.edtNoteTitle.text.toString().trim()
        val notebody = binding.edtNoteBody.text.toString().trim()
        val noteAmount = binding.ttnoteamount.text.toString().trim()
        val current  = SimpleDateFormat("dd MMM, YYYY - HH:mm")
        val formatter =current.format(Date())
//        val paynote =when(binding.ttnoteamount.text.toString().toInt()){
//             0->{"PayNote"}
//            else ->{"Notes"}
//        }

        if(noteTitle.isNotEmpty()){
            val note = Note(0,noteTitle,notebody
               , noteAmount
                           ,
                           formatter
            //                           ,paynote
            )

            notesViewmodel.addNote(note)
            Toast.makeText(mView.context,"Note is Saved",Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)

        }
        else {
            Toast.makeText(mView.context,"Please Enter Note Title",Toast.LENGTH_LONG).show()

        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_new_note,menu)
        super.onCreateOptionsMenu(menu, inflater)


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save->{
                savenote(mView)
            }
            R.id.share->{
                val sendIntent:Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    if(binding.ttnoteamount.text.toString().toInt()!=0){
                        if(binding.edtNoteAmount.text.toString().toInt()>0)
                        { putExtra(Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}\n" +
                                "You have to pay " +
                                "${binding.ttnoteamount.text} rupees to me")}
                        else {
                            putExtra(Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}\n" +
                                    "I have to pay " +
                                    "${(binding.ttnoteamount.text.toString().toInt())*(-1)} rupees to You")
                        }}
                    else {
                        if(binding.edtNoteBody.text.isNotEmpty())
                        putExtra(Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}")
                        else {
                            Toast.makeText(context,"Please Enter Some Discription or Amount ",Toast.LENGTH_LONG).show()
                        }
                    }

                    type = "text/plain"

                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                startActivity(shareIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun add(number: Int){
        AlertDialog.Builder(activity).apply {
            setTitle("Add")
            setMessage("Are you Sure you want to Add this amount?")
            setPositiveButton("Yes"){_,_,->
                binding.ttnoteamount.setText("${number+binding.ttnoteamount.text.toString().toInt()}")

            }
            setNegativeButton("No",null)
        }.create().show()
    }
    private fun sub(number: Int){
        AlertDialog.Builder(activity).apply {
            setTitle("Subtract")
            setMessage("Are you Sure you want to Subtract this amount?")
            setPositiveButton("Yes"){_,_,->
                binding.ttnoteamount.setText("${(-number)+binding.ttnoteamount.text.toString().toInt()}")

            }
            setNegativeButton("No",null)
        }.create().show()
    }

}