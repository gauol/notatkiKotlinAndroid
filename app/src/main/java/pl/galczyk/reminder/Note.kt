package pl.galczyk.reminder

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")

data class Note(
                @PrimaryKey(autoGenerate = true)
                    var id: Long?,
                @ColumnInfo(name = "username")
                    val userName: String
)