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
import java.text.SimpleDateFormat

class AddActivity : AppCompatActivity() {
    var locale = Locale.ENGLISH
    var timeZone = TimeZone.getTimeZone("UTC")
    private val c = Calendar.getInstance()
    private var mDb: NotesDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread

    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    var hour = c.get(Calendar.HOUR)
    var minute = c.get(Calendar.MINUTE)
    var date: Date = Date(c.timeInMillis)
    var time: Time = Time(c.timeInMillis)

    @SuppressLint("SimpleDateFormat")
    val timeFormat = SimpleDateFormat("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setListeners()

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = NotesDatabase.getInstance(this)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setListeners() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date = Date(c.timeInMillis)
            datePicker.text = date.toString()
        }
        datePicker.setOnClickListener {
            val dpd = DatePickerDialog(this, listener, year, month, day)
            dpd.show()
        }

        timePicker.setOnClickListener {
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)
                time = Time(c.timeInMillis)
                timePicker.text = timeFormat.format(time)
            }, hour, minute, true)
            tpd.show()
        }

        addButton.setOnClickListener{
            val  note = Note(null, nameEditText.text.toString(), descriptionEditText.text.toString(), c.timeInMillis)
            insertNoteDataInDb(note)
            val snackbar = Snackbar
                    .make(this.findViewById(android.R.id.content), note.title + " " +note.description, Snackbar.LENGTH_LONG)

            snackbar.show()
        }
    }

    private fun insertNoteDataInDb(note: Note) {
        val task = Runnable { mDb?.noteDao()?.insert(note) }
        mDbWorkerThread.postTask(task)
        finish()
    }

    override fun onDestroy() {
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
