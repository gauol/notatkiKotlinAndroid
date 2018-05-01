package pl.galczyk.reminder

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import android.util.Log
import java.sql.Date
import java.sql.Time

@Entity(tableName = "note")
class Note {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "name")
    var name: String = ""
    @ColumnInfo(name = "description")
    var description: String = ""
    @ColumnInfo(name = "date")
    var date: Date? = null
    @ColumnInfo(name = "time")
    var time: Time? = null

    constructor(name: String, description: String, date: Date, time: Time) {
        this.name = name
        this.description = description
        this.date = date
        this.time = time
    }

    fun print(): String {
        Log.d("tag", "info")
        return "$name $description $date $time"
    }
}