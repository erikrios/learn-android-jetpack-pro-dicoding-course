package com.erikriosetiawan.academy.ui.reader

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.ModuleEntity
import com.erikriosetiawan.academy.data.AcademyRepository

class CourseReaderViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    private lateinit var courseId: String
    private lateinit var moduleId: String

    fun setSelectedCourse(courseId: String) {
        this.courseId = courseId
    }

    fun setSelectedModule(moduleId: String) {
        this.moduleId = moduleId
    }

    fun getModules(): ArrayList<ModuleEntity> =
        academyRepository.getAllModulesByCourse(courseId) as ArrayList

    fun getSelectedModule(): ModuleEntity = academyRepository.getContent(courseId, moduleId)
}