package com.erikriosetiawan.academy.ui.bookmark

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.data.source.AcademyRepository
import com.erikriosetiawan.academy.utils.DataDummy

class BookmarkViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    fun getBookmarks(): List<CourseEntity> = academyRepository.getBookmarkedCourses()
}