package pl.galczyk.reminder

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener({
            val intent = Intent(this, addActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, "siemano")
            }
            startActivity(intent)
        })
    }
}
