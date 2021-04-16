package com.erikriosetiawan.academy.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.data.ModuleEntity

class DetailCourseViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    private lateinit var courseId: String

    fun setSelectedCourse(courseId: String) {
        this.courseId = courseId
    }

    fun getCourse(): LiveData<CourseEntity> = academyRepository.getCourseWithModules(courseId)

    fun getModules(): LiveData<List<ModuleEntity>> =
        academyRepository.getAllModulesByCourse(courseId)
}