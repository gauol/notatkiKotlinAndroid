package pl.galczyk.reminder

import android.arch.persistence.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAll() : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    fun deleteAllUsers()
}