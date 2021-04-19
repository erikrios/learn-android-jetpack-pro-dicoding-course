package com.erikriosetiawan.academy.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.source.local.entity.CourseEntity
import com.erikriosetiawan.academy.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    fun getCourses(): LiveData<Resource<List<CourseEntity>>> = academyRepository.getAllCourses()
}