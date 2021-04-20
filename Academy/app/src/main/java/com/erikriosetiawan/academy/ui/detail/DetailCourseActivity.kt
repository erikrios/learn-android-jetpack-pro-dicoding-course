package com.erikriosetiawan.academy.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.erikriosetiawan.academy.R
import com.erikriosetiawan.academy.data.source.local.entity.CourseEntity
import com.erikriosetiawan.academy.databinding.ActivityDetailCourseBinding
import com.erikriosetiawan.academy.databinding.ContentDetailCourseBinding
import com.erikriosetiawan.academy.ui.reader.CourseReaderActivity
import com.erikriosetiawan.academy.viewmodel.ViewModelFactory
import com.erikriosetiawan.academy.vo.Status

class DetailCourseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE = "extra_course"
    }

    private lateinit var activityDetailCourseBinding: ActivityDetailCourseBinding
    private lateinit var detailContentBinding: ContentDetailCourseBinding

    private lateinit var viewModel: DetailCourseViewModel
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailCourseBinding = ActivityDetailCourseBinding.inflate(layoutInflater)
        detailContentBinding = activityDetailCourseBinding.detailContent

        setContentView(activityDetailCourseBinding.root)

        setSupportActionBar(activityDetailCourseBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = DetailCourseAdapter()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[DetailCourseViewModel::class.java]

        val extras = intent.extras
        extras?.let {
            val courseId = extras.getString(EXTRA_COURSE)
            courseId?.let {
                viewModel.setSelectedCourse(courseId)

                viewModel.courseModule.observe(
                    this@DetailCourseActivity,
                    { courseWithModuleResource ->
                        courseWithModuleResource?.let {
                            when (courseWithModuleResource.status) {
                                Status.LOADING -> activityDetailCourseBinding.progressBar.visibility =
                                    View.VISIBLE
                                Status.SUCCESS -> courseWithModuleResource.data?.let {
                                    activityDetailCourseBinding.progressBar.visibility = View.GONE

                                    adapter.setModules(courseWithModuleResource.data.mModules)
                                    adapter.notifyDataSetChanged()
                                    populateCourse(it.mCourse)
                                }
                                Status.ERROR -> {
                                    activityDetailCourseBinding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        applicationContext,
                                        "Something went wrong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    })
            }
        }

        with(detailContentBinding.rvModule) {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@DetailCourseActivity)
            setHasFixedSize(true)
            this.adapter = adapter
            val dividerItemDecoration =
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        viewModel.courseModule.observe(this, { courseWithModule ->
            courseWithModule?.let {
                when (it.status) {
                    Status.LOADING -> activityDetailCourseBinding.progressBar.visibility =
                        View.VISIBLE
                    Status.SUCCESS -> it.data?.let {
                        activityDetailCourseBinding.progressBar.visibility = View.GONE
                        val state = courseWithModule.data?.mCourse?.bookmarked
                        setBookmarkState(state as Boolean)
                    }
                    Status.ERROR -> {
                        activityDetailCourseBinding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark) {
            viewModel.setBookmark()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateCourse(courseEntity: CourseEntity) {
        detailContentBinding.textTitle.text = courseEntity.title
        detailContentBinding.textDescription.text = courseEntity.description
        detailContentBinding.textDate.text =
            resources.getString(R.string.deadline_date, courseEntity.deadline)

        Glide.with(this)
            .load(courseEntity.imagePath)
            .transform(RoundedCorners(20))
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
            )
            .into(detailContentBinding.imagePoster)

        detailContentBinding.btnStart.setOnClickListener {
            val intent = Intent(this@DetailCourseActivity, CourseReaderActivity::class.java)
            intent.putExtra(CourseReaderActivity.EXTRA_COURSE_ID, courseEntity.courseId)
            startActivity(intent)
        }
    }

    private fun setBookmarkState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmarked_white)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_white)

        }
    }
}