package com.example.notesapp.ui.theme.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notesapp.R
import com.example.notesapp.data.models.Note
import com.example.notesapp.databinding.ItemNoteBinding
import com.example.notesapp.ui.theme.EditNote.View.EditNoteActivity

class NoteAdapter (var data: List<Note>)
    : ListAdapter<Note, NoteAdapter.MyViewHolder>(UserItemDiffCallback()) {

    class MyViewHolder(val binding: ItemNoteBinding) : ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNoteBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = data[position]
        holder.binding.note = note

        // color the card background based on the position
        when (position % 5) {
            0 -> holder.binding.card.setCardBackgroundColor(holder.binding.root.context.resources.getColor(R.color.blue))
            1 -> holder.binding.card.setCardBackgroundColor(holder.binding.root.context.resources.getColor(R.color.pink))
            2 -> holder.binding.card.setCardBackgroundColor(holder.binding.root.context.resources.getColor(R.color.orange))
            3 -> holder.binding.card.setCardBackgroundColor(holder.binding.root.context.resources.getColor(R.color.green))
            4 -> holder.binding.card.setCardBackgroundColor(holder.binding.root.context.resources.getColor(R.color.lightpink))
        }

        // when the user clicks on a note, navigate to the EditNoteActivity with the note data
        holder.binding.root.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
                putExtra("note_title", note.title)
                putExtra("note_content", note.content)
            }
            context.startActivity(intent)
        }
    }

    // updating the note list
    fun updateNotes(newNotes: List<Note>) {
        data = newNotes
        notifyDataSetChanged()
    }
}
//compare identical
class UserItemDiffCallback: DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}
