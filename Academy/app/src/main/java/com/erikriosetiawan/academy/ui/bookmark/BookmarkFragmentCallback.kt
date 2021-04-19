package com.erikriosetiawan.academy.ui.bookmark

import com.erikriosetiawan.academy.data.entity.CourseEntity

interface BookmarkFragmentCallback {
    fun onShareClick(course: CourseEntity)
}
