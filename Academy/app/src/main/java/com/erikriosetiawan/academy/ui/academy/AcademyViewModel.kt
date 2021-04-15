package com.erikriosetiawan.academy.ui.academy

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.data.AcademyRepository

class AcademyViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    fun getCourses(): List<CourseEntity> = academyRepository.getAllCourses()
}