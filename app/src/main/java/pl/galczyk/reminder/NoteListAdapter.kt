package pl.galczyk.reminder

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_note.view.*

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private var listOfNote: MutableList<Note> = mutableListOf()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOfNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listOfNote[position]
        holder.noteNameTextView.text = note.title
        holder.noteLayout.setOnClickListener({
            context?.startActivity(Intent(context, noteDetailsActivity::class.java)
                    .putExtra("note", note))
        })
        setupSwipeDelete(holder.noteItemSwipeLayout)
        setupBottomViewClick(holder.bottomWrapperLayout, note)
    }

    private fun setupBottomViewClick(bottomWrapperLayout: FrameLayout?, note: note) {
        bottomWrapperLayout?.setOnClickListener({
            deletenoteFromList(note)
        })
    }

    private fun deletenoteFromList(note: Note) {
        listOfNote.remove(note)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitleTextView = view.itemNameTextView
        val noteDescriptionTextView = view.itemDescriptionTextView
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