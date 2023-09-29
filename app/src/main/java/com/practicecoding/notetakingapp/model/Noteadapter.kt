package com.practicecoding.notetakingapp.model

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicecoding.notetakingapp.HomeFragment
import com.practicecoding.notetakingapp.HomeFragmentDirections
import com.practicecoding.notetakingapp.R
import com.practicecoding.notetakingapp.databinding.NoteLayoutBinding
import com.practicecoding.notetakingapp.room.Note
import java.util.*
import kotlin.properties.Delegates

class Noteadapter(private val showdeletemenu: (Boolean) -> Unit) :
    RecyclerView.Adapter<Noteadapter.Noteviewholder>() {
    private var isEnable = false
    lateinit var itemselectedList: Note
    private lateinit var currentNote: Note
    var place: Int = 0

    inner class Noteviewholder(val itemBinding: NoteLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differcallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteBody == newItem.noteBody &&
                    oldItem.noteTitle == newItem.noteTitle
                    &&
                    oldItem.noteAmount == newItem.noteAmount
                    &&
                    oldItem.timeStamp == newItem.timeStamp
            //                    &&
//                    oldItem.paynote==newItem.paynote
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this@Noteadapter, differcallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Noteviewholder {
        return Noteviewholder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: Noteviewholder, position: Int) {
        currentNote = differ.currentList[position]
        holder.itemBinding.tvnotetitle.text = currentNote.noteTitle
        if (currentNote.noteBody.isNotEmpty())
            holder.itemBinding.tvNotebody.text = currentNote.noteBody
        else holder.itemBinding.tvNotebody.text = "No Discription"
        if (currentNote.noteAmount.toInt() != 0) {
            holder.itemBinding.tvnoteamount.text = currentNote.noteAmount
            holder.itemBinding.noteType.text = "PayNote"
        } else {
            holder.itemBinding.noteType.text = "Notes"
        }
//
        holder.itemBinding.time.text = currentNote.timeStamp
        val random = Random()
        val color = Color.argb(
            255, random.nextInt(256),
            random.nextInt(256), random.nextInt(256)
        )
        holder.itemBinding.ibColor.setBackgroundColor(color)
        holder.itemView.setOnClickListener {

            if (isEnable) {
                isEnable = false
                holder.itemBinding.imageSelect.visibility = View.GONE
                showdeletemenu(isEnable)
            } else {
                selecedItem(position)

                val direction =
                    HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(differ.currentList[place])

                it.findNavController().navigate(direction)
            }
        }
        holder.itemView.setOnLongClickListener() {
            isEnable = true
            holder.itemBinding.imageSelect.visibility = View.VISIBLE
            showdeletemenu(true)

            selecedItem(position)
            true
        }
    }

    fun updatenote(): Note {
        itemselectedList = differ.currentList[place]

        return differ.currentList[place]
    }


    fun selecedItem(position: Int){
        place = position
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}


