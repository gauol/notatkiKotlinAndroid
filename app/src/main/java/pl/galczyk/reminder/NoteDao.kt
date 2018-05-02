package pl.galczyk.reminder

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Data Access Object for the users table.
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll() : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("DELETE FROM notes")
    fun deleteAllUsers()
}