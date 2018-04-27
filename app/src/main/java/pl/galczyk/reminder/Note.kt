package pl.galczyk.reminder

import android.util.Log
import java.sql.Date
import java.sql.Time

data class Note(
val name: String,
val description: String,
val date: Date,
val time: Time
){
    fun print(): String {
        Log.d("tag", "info")
        return "$name $description $date $time"
    }
}