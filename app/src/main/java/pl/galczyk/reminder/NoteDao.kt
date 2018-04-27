package pl.galczyk.reminder

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface UserDao {
    @get:Query("SELECT * FROM notes")
    val all: List<Note>

    @Query("SELECT * FROM notes WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Note>

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)
}