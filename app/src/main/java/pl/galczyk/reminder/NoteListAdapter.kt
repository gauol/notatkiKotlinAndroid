package pl.galczyk.reminder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat

class NoteListAdapter(var mainActivity: MainActivity) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private var listOfNote: MutableList<Note> = mutableListOf()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

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
        val date = note.date
        holder.noteDateTextView.text = dateFormat.format(date)
        if (position % 2 == 0)
            holder.noteLayout.setBackgroundColor(Color.LTGRAY)
//        else
//            holder.noteLayout.setBackgroundColor(Color.YELLOW)

        holder.noteLayout.setOnClickListener({
            AlertDialog.Builder(mainActivity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Usuwanie")
                    .setMessage("Czy na pewno chcesz usunąć notatkę?")
                    .setPositiveButton("Tak", { dialog, which ->
                        mainActivity.deleteNote(note)
                        deletenoteFromList(note)
                    }) //TODO: dodac usuwane z bazy danych
                    .setNegativeButton("Nie", null)
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
        val noteDateTextView = view.itemDateTextView
        val noteLayout = view.noteLauout
    }

    fun setListOfnote(listOfnote: MutableList<Note>) {
        this.listOfNote = listOfnote
        notifyDataSetChanged()
    }

    fun deleteAllNotes() {
        this.listOfNote.clear()
        notifyDataSetChanged()
    }
}