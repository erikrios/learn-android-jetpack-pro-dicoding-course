package com.erikriosetiawan.academy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.di.Injection
import com.erikriosetiawan.academy.ui.academy.AcademyViewModel
import com.erikriosetiawan.academy.ui.bookmark.BookmarkViewModel
import com.erikriosetiawan.academy.ui.detail.DetailCourseViewModel
import com.erikriosetiawan.academy.ui.reader.CourseReaderViewModel

class ViewModelFactory private constructor(private val mAcademyRepository: AcademyRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                instance = this
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AcademyViewModel::class.java) -> AcademyViewModel(
                mAcademyRepository
            ) as T
            modelClass.isAssignableFrom(DetailCourseViewModel::class.java) -> DetailCourseViewModel(
                mAcademyRepository
            ) as T
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> BookmarkViewModel(
                mAcademyRepository
            ) as T
            modelClass.isAssignableFrom(CourseReaderViewModel::class.java) -> CourseReaderViewModel(
                mAcademyRepository
            ) as T
            else -> throw Throwable("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}