package pl.galczyk.reminder

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.sql.Date
import java.sql.Time
import java.util.*
import android.support.design.widget.Snackbar




class addActivity : AppCompatActivity() {
    val c = Calendar.getInstance()!!
    private var mDb: NotesDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        setListeners(year, month, day, hour, minute)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = NotesDatabase.getInstance(this)

    }

    @SuppressLint("SetTextI18n")
    private fun setListeners(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        datePicker.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                datePicker.text = "$dayOfMonth.$monthOfYear.$year"
            }, year, month, day)
            dpd.show()
        }

        timePicker.setOnClickListener {
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                timePicker.text = "$hour:$minute"
            }, hour, minute, true)
            tpd.show()
        }

        addButton.setOnClickListener{
            var date = Date(c.timeInMillis)
            var time = Time(c.timeInMillis)
            //var  note = Note(nameEditText.text.toString(), descriptionEditText.text.toString(), date, time)
            val  note = Note(null, nameEditText.text.toString())

            insertNoteDataInDb(note)

            val snackbar = Snackbar
                    .make(this.findViewById(android.R.id.content), note.userName, Snackbar.LENGTH_LONG)

            snackbar.show()
        }
    }

    private fun insertNoteDataInDb(note: Note) {
        val task = Runnable { mDb?.noteDao()?.insert(note) }
        mDbWorkerThread.postTask(task)
    }

    override fun onDestroy() {
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
