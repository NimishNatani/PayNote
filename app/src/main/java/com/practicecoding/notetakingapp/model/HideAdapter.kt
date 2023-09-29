package com.practicecoding.notetakingapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicecoding.notetakingapp.Hidehome
import com.practicecoding.notetakingapp.HidehomeDirections
import com.practicecoding.notetakingapp.databinding.NoteLayoutBinding
import com.practicecoding.notetakingapp.room.Note
import com.practicecoding.notetakingapp.room.NoteHide
import java.util.Random

class HideAdapter(private val showdeletemenu: (Boolean)->Unit): RecyclerView.Adapter<HideAdapter.HideNoteviewholder>() {
    private var isEnable =false
    lateinit var  itemselectedList:NoteHide
    private lateinit var currentHideNote: NoteHide
    var place:Int =0

    inner class HideNoteviewholder(val itemBinding: NoteLayoutBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    private val differhidecallback = object : DiffUtil.ItemCallback<NoteHide>(){

        override fun areContentsTheSame(oldItem: NoteHide, newItem: NoteHide): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: NoteHide, newItem: NoteHide): Boolean {
            return oldItem.id==newItem.id&&
                    oldItem.noteBody==newItem.noteBody&&
                    oldItem.noteTitle==newItem.noteTitle
                    &&
                    oldItem.noteAmount==newItem.noteAmount
                    &&
                    oldItem.timeStamp==newItem.timeStamp        }
    }
    val hidediffer = AsyncListDiffer(this@HideAdapter,differhidecallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HideNoteviewholder {
        return HideNoteviewholder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: HideNoteviewholder, position: Int) {
        currentHideNote = hidediffer.currentList[position]
        holder.itemBinding.tvnotetitle.text = currentHideNote.noteTitle
        if(currentHideNote.noteBody.isNotEmpty())
            holder.itemBinding.tvNotebody.text = currentHideNote.noteBody
        else holder.itemBinding.tvNotebody.text = "No Discription"
        if(currentHideNote.noteAmount.toInt()!=0){
            holder.itemBinding.tvnoteamount.text = currentHideNote.noteAmount
            holder.itemBinding.noteType.text ="PayNote"
        }
        else {
            holder.itemBinding.noteType.text = "Notes"
        }
//
        holder.itemBinding.time.text = currentHideNote.timeStamp
        val random = Random()
        val color = Color.argb(
            255,random.nextInt(256),
            random.nextInt(256),random.nextInt(256)
        )
        holder.itemBinding.ibColor.setBackgroundColor(color)
        holder.itemView.setOnClickListener{

            if(isEnable){
                isEnable = false
                holder.itemBinding.imageSelect.visibility = View.GONE
                showdeletemenu(isEnable)
            }
            else{val direction = HidehomeDirections.
            actionHidehomeToHideUpdateFragment(currentHideNote)

                it.findNavController().navigate(direction)
            }}
        holder.itemView.setOnLongClickListener(){

            selecedItem(holder,currentHideNote,position)
            true
        }

    }
    fun updatenote(): NoteHide {
        itemselectedList = hidediffer.currentList[place]

        return hidediffer.currentList[place]
    }



    private fun selecedItem(holder: HideNoteviewholder, currentNote: NoteHide?, position: Int) {
        isEnable=true
        holder.itemBinding.imageSelect.visibility = View.VISIBLE
        place=position
        showdeletemenu(true)
    }

    override fun getItemCount(): Int {
        return hidediffer.currentList.size
    }
}