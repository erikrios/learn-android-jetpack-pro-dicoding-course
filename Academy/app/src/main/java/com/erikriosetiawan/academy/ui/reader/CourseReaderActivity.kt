package com.erikriosetiawan.academy.ui.reader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erikriosetiawan.academy.R
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.ui.reader.content.ModuleContentFragment
import com.erikriosetiawan.academy.ui.reader.list.ModuleListFragment
import com.erikriosetiawan.academy.viewmodel.ViewModelFactory
import com.erikriosetiawan.academy.vo.Resource
import com.erikriosetiawan.academy.vo.Status

class CourseReaderActivity : AppCompatActivity(), CourseReaderCallback {

    companion object {
        const val EXTRA_COURSE_ID = "extra_course_id"
    }

    private var isLarge = false
    private lateinit var viewModel: CourseReaderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_reader)

        if (findViewById<View>(R.id.frame_list) != null) {
            isLarge = true
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[CourseReaderViewModel::class.java]
        viewModel.modules.observe(this, initObserver)

        val bundle = intent.extras
        bundle?.let {
            val courseId = bundle.getString(EXTRA_COURSE_ID)
            courseId?.let {
                viewModel.setSelectedCourse(courseId)
                populateFragment()
            }
        }
    }

    override fun moveTo(position: Int, moduleId: String) {
        if (!isLarge) {
            val fragment = ModuleContentFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_container, fragment, ModuleContentFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun populateFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!isLarge) {
            var fragment = supportFragmentManager.findFragmentByTag(ModuleListFragment.TAG)
            if (fragment == null) {
                fragment = ModuleListFragment.newInstance()
                fragmentTransaction.add(R.id.frame_container, fragment, ModuleListFragment.TAG)
                fragmentTransaction.addToBackStack(null)
            }
            fragmentTransaction.commit()
        } else {
            var fragmentList = supportFragmentManager.findFragmentByTag(ModuleListFragment.TAG)

            if (fragmentList == null) {
                fragmentList = ModuleListFragment.newInstance()
                fragmentTransaction.add(
                    R.id.fragment_container_view_tag,
                    fragmentList,
                    ModuleListFragment.TAG
                )
            }

            var fragmentContent =
                supportFragmentManager.findFragmentByTag(ModuleContentFragment.TAG)
            if (fragmentContent == null) {
                fragmentContent = ModuleContentFragment.newInstance()
                fragmentTransaction.add(
                    R.id.frame_container,
                    fragmentContent,
                    ModuleContentFragment.TAG
                )
            }
            fragmentTransaction.commit()
        }
    }

    private fun remoteObserver() {
        viewModel.modules.removeObserver(initObserver)
    }

    private fun getLastReadFromModules(moduleEntities: List<ModuleEntity>): String? {
        var lastReadModule: String? = null
        for (moduleEntity in moduleEntities) {
            if (moduleEntity.read) {
                lastReadModule = moduleEntity.moduleId
                continue
            }
            break
        }
        return lastReadModule
    }

    private val initObserver: Observer<Resource<List<ModuleEntity>>> = Observer { modules ->
        modules?.let {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val dataModules: List<ModuleEntity>? = modules.data
                    if (dataModules != null && dataModules.isNotEmpty()) {

                        val firstModule = dataModules[0]
                        val isFirstModuleRead = firstModule.read

                        if (!isFirstModuleRead) {
                            val firstModuleId = firstModule.moduleId
                            viewModel.setSelectedModule(firstModuleId)
                        } else {
                            val lastReadModuleId = getLastReadFromModules(dataModules)
                            if (lastReadModuleId != null) {
                                viewModel.setSelectedModule(lastReadModuleId)
                            }
                        }
                    }
                    remoteObserver()
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Failed to display data.", Toast.LENGTH_SHORT).show()
                    remoteObserver()
                }
            }
        }
    }
}