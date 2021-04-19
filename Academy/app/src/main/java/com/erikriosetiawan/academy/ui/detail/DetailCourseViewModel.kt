package com.erikriosetiawan.academy.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.source.local.entity.CourseEntity
import com.erikriosetiawan.academy.data.source.local.entity.CourseWithModule
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.vo.Resource

class DetailCourseViewModel(private val academyRepository: AcademyRepository) : ViewModel() {

    val courseId = MutableLiveData<String>()

    fun setSelectedCourse(courseId: String) {
        this.courseId.value = courseId
    }

    var courseModule: LiveData<Resource<CourseWithModule>> = Transformations.switchMap(courseId) {mCourseId ->
        academyRepository.getCourseWithModules(mCourseId)
    }

    fun setBookmark() {
        val moduleResource = courseModule.value
        moduleResource?.let {
            val courseWithModule = moduleResource.data

            courseWithModule?.let {
                val courseEntity = courseWithModule.mCourse
                val newState = !courseEntity.bookmarked
                academyRepository.setCourseBookmark(courseEntity, newState)
            }
        }
    }
}