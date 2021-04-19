package com.erikriosetiawan.academy.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.vo.Resource

class CourseReaderViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    val courseId = MutableLiveData<String>()
    val moduleId = MutableLiveData<String>()

    fun setSelectedCourse(courseId: String) {
        this.courseId.value = courseId
    }

    fun setSelectedModule(moduleId: String) {
        this.moduleId.value = moduleId
    }

    var modules: LiveData<Resource<List<ModuleEntity>>> =
        Transformations.switchMap(courseId) { mCourseId ->
            academyRepository.getAllModulesByCourse(mCourseId)
        }

    var selectedModule: LiveData<Resource<ModuleEntity>> =
        Transformations.switchMap(moduleId) { selectedPosition ->
            academyRepository.getContent(selectedPosition)
        }

    fun readContent(module: ModuleEntity) {
        academyRepository.setReadModule(module)
    }

    fun getModuleSize(): Int {
        modules.value?.let {
            val moduleEntities = modules.value?.data
            moduleEntities?.let {
                return moduleEntities.size
            }
        }
        return 0
    }

    fun setNextPage() {
        if (selectedModule.value != null && modules.value != null) {
            val moduleEntity = selectedModule.value?.data
            val moduleEntities = modules.value?.data
            if (moduleEntity != null && moduleEntities != null) {
                val position = moduleEntity.position
                if (position < moduleEntities.size && position >= 0) {
                    moduleId.value = moduleEntities[position + 1].moduleId
                }
            }
        }
    }

    fun setPrevPage() {
        if (selectedModule.value != null && modules.value != null) {
            val moduleEntity = selectedModule.value?.data
            val moduleEntities = modules.value?.data
            if (moduleEntity != null && moduleEntities != null) {
                val position = moduleEntity.position
                if (position < moduleEntities.size && position >= 0) {
                    moduleId.value = moduleEntities[position - 1].moduleId
                }
            }
        }
    }
}