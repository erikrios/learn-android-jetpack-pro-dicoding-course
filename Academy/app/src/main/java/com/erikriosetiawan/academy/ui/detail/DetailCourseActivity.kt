package com.erikriosetiawan.academy.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erikriosetiawan.academy.databinding.ActivityDetailCourseBinding
import com.erikriosetiawan.academy.databinding.ContentDetailCourseBinding

class DetailCourseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }

    private lateinit var detailContentBinding: ContentDetailCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityDetailCourseBinding = ActivityDetailCourseBinding.inflate(layoutInflater)
        detailContentBinding = activityDetailCourseBinding.detailContent

        setContentView(activityDetailCourseBinding.root)

        setSupportActionBar(activityDetailCourseBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}