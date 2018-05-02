package pl.galczyk.reminder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()  {
    private var mDb: NotesDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = NotesDatabase.getInstance(this)
        addButton.setOnClickListener({
            val intent = Intent(this, addActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "siemano")
            }
            startActivity(intent)
        })

        getButton.setOnClickListener({
            getNotesFromDB()
        })

            getNotesFromDB()


    }

    private fun getNotesFromDB() {
        val task = Runnable {
            Log.d("info", "info")
            val notes =
                    mDb?.noteDao()?.getAll()
            mUiHandler.post({
                if (notes == null || notes.isEmpty()) {
                    Snackbar.make(this.findViewById(android.R.id.content), "no data", Snackbar.LENGTH_LONG).show()
                } else {
                    var value =""
                    for (note in notes){
                        value += note.userName
                    }
                    notesTextView.text = value
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    private fun getNotesFromDBThread() {
        Thread({
            val notes = mDb?.noteDao()?.getAll()
            if (notes == null || notes.isEmpty()) {
                Snackbar.make(this.findViewById(android.R.id.content), "no data", Snackbar.LENGTH_LONG).show()
            } else {
                var value =""
                for (note in notes){
                    value += note.userName + "\r\n"
                }
                notesTextView.text = value
            }
        }).start()
    }

    override fun onDestroy() {
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
