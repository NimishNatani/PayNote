package com.practicecoding.notetakingapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicecoding.notetakingapp.HomeFragment
import com.practicecoding.notetakingapp.HomeFragmentDirections
import com.practicecoding.notetakingapp.databinding.NoteLayoutBinding
import com.practicecoding.notetakingapp.room.Note
import java.util.*

class Noteadapter:RecyclerView.Adapter<Noteadapter.Noteviewholder>() {
    inner class Noteviewholder(val itemBinding:NoteLayoutBinding):
            RecyclerView.ViewHolder(itemBinding.root)

    private val differcallback = object :DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id==newItem.id&&
                    oldItem.noteBody==newItem.noteBody&&
                    oldItem.noteTitle==newItem.noteTitle
                    &&
            oldItem.noteAmount==newItem.noteAmount
            &&
                    oldItem.timeStamp==newItem.timeStamp
        //                    &&
//                    oldItem.paynote==newItem.paynote
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this@Noteadapter,differcallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Noteviewholder {
        return Noteviewholder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: Noteviewholder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.itemBinding.tvnotetitle.text = currentNote.noteTitle
        if(currentNote.noteBody.isNotEmpty())
        holder.itemBinding.tvNotebody.text = currentNote.noteBody
        else holder.itemBinding.tvNotebody.text = "No Discription"
        if(currentNote.noteAmount.toInt()!=0){
        holder.itemBinding.tvnoteamount.text = currentNote.noteAmount
        holder.itemBinding.noteType.text ="PayNote"
        }
        else {
            holder.itemBinding.noteType.text = "Notes"
        }
//
        holder.itemBinding.time.text = currentNote.timeStamp
        val random = Random()
        val color = Color.argb(
            255,random.nextInt(256),
            random.nextInt(256),random.nextInt(256)
        )
        holder.itemBinding.ibColor.setBackgroundColor(color)
        holder.itemView.setOnClickListener{
            val direction = HomeFragmentDirections.
            actionHomeFragmentToUpdateNoteFragment(currentNote)

            it.findNavController().navigate(direction)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}