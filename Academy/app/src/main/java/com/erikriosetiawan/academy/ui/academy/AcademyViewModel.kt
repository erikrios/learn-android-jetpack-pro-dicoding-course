package com.erikriosetiawan.academy.ui.academy

import androidx.lifecycle.ViewModel
import com.erikriosetiawan.academy.data.CourseEntity
import com.erikriosetiawan.academy.utils.DataDummy

class AcademyViewModel : ViewModel() {
    fun getCourses(): List<CourseEntity> = DataDummy.generateDummyCourses()
}