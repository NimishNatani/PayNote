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
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.practicecoding.notetakingapp.databinding.FragmentHidenewfragmentBinding
import com.practicecoding.notetakingapp.databinding.FragmentNewNoteBinding
import com.practicecoding.notetakingapp.model.HideAdapter
import com.practicecoding.notetakingapp.model.HideNoteviewmodel
import com.practicecoding.notetakingapp.model.Noteadapter
import com.practicecoding.notetakingapp.model.Noteviewmodel
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide
import java.text.SimpleDateFormat
import java.util.Date

class Hidenewfragment : Fragment(R.layout.fragment_hidenewfragment) {

    private var _binding: FragmentHidenewfragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var  notesViewmodel: HideNoteviewmodel
var note1:NoteHide? =null
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
        _binding = FragmentHidenewfragmentBinding.inflate(inflater,container,false)
        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewmodel = (activity as MainActivity).hideNoteviewmodel
        mView = view
        binding.add1.setOnClickListener(){
            if(binding.edtNoteAmount.text.isNotEmpty()){
                add(binding.edtNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount", Toast.LENGTH_LONG).show()

            }
        }
        binding.minus1.setOnClickListener(){
            if(binding.edtNoteAmount.text.isNotEmpty()){
                sub(binding.edtNoteAmount.text.toString().toInt())
            }
            else {
                Toast.makeText(context,"Please Enter Amount", Toast.LENGTH_LONG).show()

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
            val note = NoteHide(0,noteTitle,notebody
                , noteAmount
                ,
                formatter
                //                           ,paynote
            )

            notesViewmodel.addhideNote(note)
            Toast.makeText(mView.context,"Note is Saved", Toast.LENGTH_LONG).show()
            view.findNavController().navigate(R.id.action_hidenewfragment_to_hidehome)

        }
        else {
            Toast.makeText(mView.context,"Please Enter Note Title", Toast.LENGTH_LONG).show()

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
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    if(binding.ttnoteamount.text.toString().toInt()!=0){
                        if(binding.edtNoteAmount.text.toString().toInt()>0)
                        { putExtra(
                            Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}\n" +
                                "You have to pay " +
                                "${binding.ttnoteamount.text} rupees to me")}
                        else {
                            putExtra(
                                Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}\n" +
                                    "I have to pay " +
                                    "${(binding.ttnoteamount.text.toString().toInt())*(-1)} rupees to You")
                        }}
                    else {
                        if(binding.edtNoteBody.text.isNotEmpty())
                            putExtra(Intent.EXTRA_TEXT,"${binding.edtNoteBody.text}")
                        else {
                            Toast.makeText(context,"Please Enter Some Discription or Amount ",
                                Toast.LENGTH_LONG).show()
                        }
                    }

                    type = "text/plain"

                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                startActivity(shareIntent)
            }
            R.id.notification->{
                val noteTitle = binding.edtNoteTitle.text.toString().trim()
                val notebody = binding.edtNoteBody.text.toString().trim()
                val noteAmount = binding.ttnoteamount.text.toString().trim()
                val current  = SimpleDateFormat("dd MMM, YYYY - HH:mm")
                val formatter =current.format(Date())
                if(noteTitle.isNotEmpty()){
                    note1 = NoteHide(0,noteTitle,notebody
                        , noteAmount
                        ,
                        formatter
                        //                           ,paynote
                    )

                    val direction = NewNoteFragmentDirections.
                    actionNewNoteFragmentToNotificationFragment(null,note1)
                    findNavController().navigate(direction)}
                else {Toast.makeText(context,"Please add some note title",Toast.LENGTH_LONG).show()}

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