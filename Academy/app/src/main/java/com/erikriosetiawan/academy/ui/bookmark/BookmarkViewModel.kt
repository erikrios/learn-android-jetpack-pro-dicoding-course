package com.erikriosetiawan.academy.ui.bookmark

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.data.AcademyRepository

class BookmarkViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    fun getBookmarks(): List<CourseEntity> = academyRepository.getBookmarkedCourses()
}