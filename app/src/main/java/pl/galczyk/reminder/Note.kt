package pl.galczyk.reminder

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "notes")

data class Note(
                @PrimaryKey(autoGenerate = true)
                    var id: Long?,
                @ColumnInfo(name = "title")
                    val title: String,
                @ColumnInfo(name = "description")
                    val description: String,
                @ColumnInfo(name = "date")
                    val date: String
)