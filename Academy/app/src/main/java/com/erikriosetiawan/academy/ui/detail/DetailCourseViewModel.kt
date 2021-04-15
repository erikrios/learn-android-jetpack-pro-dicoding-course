package com.erikriosetiawan.academy.ui.detail

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.data.ModuleEntity
import com.erikriosetiawan.academy.data.source.AcademyRepository

class DetailCourseViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    private lateinit var courseId: String

    fun setSelectedCourse(courseId: String) {
        this.courseId = courseId
    }

    fun getCourse(): CourseEntity = academyRepository.getCourseWithModules(courseId)

    fun getModules(): List<ModuleEntity> = academyRepository.getAllModulesByCourse(courseId)
}