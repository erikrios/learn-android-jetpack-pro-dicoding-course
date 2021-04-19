package com.erikriosetiawan.academy.data.source.local.entity

import androidx.room.Entity

@Entity(tableName = "courseentities")
data class CourseEntity(
    var courseId: String,
    var title: String,
    var description: String,
    var deadline: String,
    var bookmarked: Boolean = false,
    var imagePath: String
)