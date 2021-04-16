package com.erikriosetiawan.academy.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.CourseEntity

class BookmarkViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    fun getBookmarks(): LiveData<List<CourseEntity>> = academyRepository.getBookmarkedCourses()
}