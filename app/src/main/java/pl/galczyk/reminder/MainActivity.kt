package pl.galczyk.reminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()  {
    private var mDb: NotesDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    private var notesList: MutableList<Note> = mutableListOf()
    private var noteAdapter: NoteListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = NotesDatabase.getInstance(this)

        addButton.setOnClickListener({
            val intent = Intent(this, AddActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "siemano")
            }
            startActivity(intent)
        })

        getButton.setOnClickListener({
            AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Czy na pewno?")
                    .setMessage("Czy na pewno chcesz usunąć wszystkie notatki?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which -> deleteAllNotes() })
                    .setNegativeButton("No", null)
                    .show()
        })
    }

    private fun setupNoteAdapter(){
        noteAdapter = NoteListAdapter(this)
        noteListRecyclerView.layoutManager = LinearLayoutManager(this)
        noteListRecyclerView.adapter = noteAdapter
        noteAdapter?.setListOfnote(notesList)
    }

    override fun onResume() {
        super.onResume()
        getNotesFromDB()
    }

    private fun getNotesFromDB() {
        val task = Runnable {
            notesList = mDb?.noteDao()?.getAll()!!
            mUiHandler.post({
                if (notesList.isEmpty()) {
                    schowSnackbarMessage("Brak notatek")
                }else{

                }
                setupNoteAdapter()
            })
        }
        mDbWorkerThread.postTask(task)
    }

    private fun deleteAllNotes(){
        val task = Runnable {
            mDb?.noteDao()?.deleteAllNotes()
            mUiHandler.post({
                noteAdapter?.deleteAllNotes()
                schowSnackbarMessage("Usunięto wszystkie notatki")
            })
        }
        mDbWorkerThread.postTask(task)
    }

    fun deleteNote(note: Note){
        val task = Runnable {
            mDb?.noteDao()?.deleteNote(note)
            mUiHandler.post({
                schowSnackbarMessage("Usunięto wiadomość")
            })
        }
        mDbWorkerThread.postTask(task)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDbWorkerThread.quit()
    }

    fun schowSnackbarMessage(message: String){
        Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    fun showNotification(id: Int, text: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentText(text)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notificationBuilder.build())
    }
}
