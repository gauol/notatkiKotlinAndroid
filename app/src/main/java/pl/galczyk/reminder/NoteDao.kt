package pl.galczyk.reminder

import android.arch.persistence.room.*


@Dao
interface NoteDao {
    @get:Query("SELECT * FROM note")
    val all: List<Note>

    @Query("SELECT * FROM note WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)
}