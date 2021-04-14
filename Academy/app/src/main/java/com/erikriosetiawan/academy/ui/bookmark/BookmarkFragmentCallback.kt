package com.erikriosetiawan.academy.ui.bookmark

import com.erikriosetiawan.academy.data.CourseEntity

interface BookmarkFragmentCallback {
    fun onShareClick(course: CourseEntity)
}
