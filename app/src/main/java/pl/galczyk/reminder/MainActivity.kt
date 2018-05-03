package pl.galczyk.reminder

import android.content.Intent
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


//    private lateinit var viewModelFactory: ViewModelFactory
//    private lateinit var viewModel: UserViewModel
//    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = NotesDatabase.getInstance(this)

//        viewModelFactory = Injection.provideViewModelFactory(this)
//
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        addButton.setOnClickListener({
            val intent = Intent(this, addActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "siemano")
            }
            startActivity(intent)
        })

        getButton.setOnClickListener({
            getNotesFromDB()
        })

           // getNotesFromDB()
    }

    override fun onResume() {
        super.onResume()
        getNotesFromDB()
        Log.d("start", "resume")

    }

    override fun onStart() {
        super.onStart()
        getNotesFromDB()
        Log.d("start", "onstasrt")
    }
//
//    private fun updateUserName() {
//        val userName = user_name_input.text.toString()
//        // Disable the update button until the user name update has been done
//        update_user_button.isEnabled = false
//        // Subscribe to updating the user name.
//        // Enable back the button once the user name has been updated
//        disposable.add(viewModel.updateUserName(userName)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ update_user_button.isEnabled = true },
//                        { error -> Log.e(TAG, "Unable to update username", error) }))
//    }

    private fun getNotesFromDB() {
        val task = Runnable {
            //Log.d("info", "info")
            val notes =
                    mDb?.noteDao()?.getAll()
            mUiHandler.post({
                if (notes == null || notes.isEmpty()) {
                    Snackbar.make(this.findViewById(android.R.id.content), "no data", Snackbar.LENGTH_LONG).show()
                } else {
                    var value =""
                    for (note in notes){
                        value += note.userName + "\r\n"
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
        //disposable.clear()
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
