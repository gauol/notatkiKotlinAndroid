package pl.galczyk.reminder

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*
import android.content.DialogInterface
import android.support.v7.appcompat.R.styleable.AlertDialog



class NoteListAdapter(var mainActivity: MainActivity) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private var listOfNote: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOfNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listOfNote[position]
        holder.noteTitleTextView.text = note.title
        holder.noteDescriptionTextView.text = note.description
        if(position%2 ==1)
            holder.noteLayout.setBackgroundColor(Color.MAGENTA)
        else
            holder.noteLayout.setBackgroundColor(Color.YELLOW)
        holder.noteLayout.setOnClickListener({
//            context?.startActivity(Intent(context, mainActivity::class.java)
//                    .putExtra("note", note))
            AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which -> finish() })
                    .setNegativeButton("No", null)
                    .show()
        })
    }

    private fun deletenoteFromList(note: Note) {
        listOfNote.remove(note)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitleTextView = view.itemNameTextView
        val noteDescriptionTextView = view.itemDescriptionTextView
        val noteLayout = view.noteLauout
    }

    fun setListOfnote(listOfnote: MutableList<Note>) {
        this.listOfNote = listOfnote
        notifyDataSetChanged()
    }

    fun addnote(note: Note) {
        this.listOfNote.add(note)
        notifyDataSetChanged()
    }
}