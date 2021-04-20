package com.erikriosetiawan.mynoteapps.repository

import android.app.Application
import androidx.paging.DataSource
import com.erikriosetiawan.mynoteapps.database.Note
import com.erikriosetiawan.mynoteapps.database.NoteDao
import com.erikriosetiawan.mynoteapps.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val mNotesDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNotesDao = db.noteDao()
    }

    fun getAllNotes(): DataSource.Factory<Int, Note> = mNotesDao.getAllNotes()

    fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
}