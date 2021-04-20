package com.erikriosetiawan.academy.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.erikriosetiawan.academy.data.source.AcademyDataSource
import com.erikriosetiawan.academy.data.source.local.LocalDataSource
import com.erikriosetiawan.academy.data.source.local.entity.CourseEntity
import com.erikriosetiawan.academy.data.source.local.entity.CourseWithModule
import com.erikriosetiawan.academy.data.source.local.entity.ModuleEntity
import com.erikriosetiawan.academy.data.source.remote.ApiResponse
import com.erikriosetiawan.academy.data.source.remote.RemoteDataSource
import com.erikriosetiawan.academy.data.source.remote.response.ContentResponse
import com.erikriosetiawan.academy.data.source.remote.response.CourseResponse
import com.erikriosetiawan.academy.data.source.remote.response.ModuleResponse
import com.erikriosetiawan.academy.utils.AppExecutors
import com.erikriosetiawan.academy.vo.Resource

class FakeAcademyRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    AcademyDataSource {

    override fun getAllCourses(): LiveData<Resource<PagedList<CourseEntity>>> {
        return object :
            NetworkBoundResource<PagedList<CourseEntity>, List<CourseResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<CourseEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllCourses(), config).build()
            }

            override fun shouldFetch(data: PagedList<CourseEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<CourseResponse>>> =
                remoteDataSource.getAllCourses()

            override fun saveCallResult(data: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in data) {
                    val course = CourseEntity(
                        response.id,
                        response.title,
                        response.description,
                        response.date,
                        false,
                        response.imagePath
                    )

                    courseList.add(course)
                }
            }
        }.asLiveData()
    }

    override fun getBookmarkedCourses(): LiveData<PagedList<CourseEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getBookmarkedCourses(), config).build()
    }

    override fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModule>> {

        return object : NetworkBoundResource<CourseWithModule, List<ModuleResponse>>(appExecutors) {

            override fun loadFromDB(): LiveData<CourseWithModule> =
                localDataSource.getCourseWithModules(courseId)

            override fun shouldFetch(data: CourseWithModule?): Boolean =
                data?.mModules == null || data.mModules.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getModules(courseId)

            override fun saveCallResult(data: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in data) {
                    val course = ModuleEntity(
                        response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false
                    )

                    moduleList.add(course)
                }

                localDataSource.insertModules(moduleList)
            }
        }.asLiveData()
    }

    override fun getAllModulesByCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>> {
        return object :
            NetworkBoundResource<List<ModuleEntity>, List<ModuleResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<ModuleEntity>> =
                localDataSource.getAllModulesByCourse(courseId)

            override fun shouldFetch(data: List<ModuleEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getModules(courseId)

            override fun saveCallResult(data: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in data) {
                    val course = ModuleEntity(
                        response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false
                    )
                    moduleList.add(course)
                }
                localDataSource.insertModules(moduleList)
            }
        }.asLiveData()
    }

    override fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>> {
        return object : NetworkBoundResource<ModuleEntity, ContentResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<ModuleEntity> =
                localDataSource.getModuleWithContent(moduleId)

            override fun shouldFetch(data: ModuleEntity?): Boolean = data?.contentEntity == null

            override fun createCall(): LiveData<ApiResponse<ContentResponse>> =
                remoteDataSource.getContent(moduleId)

            override fun saveCallResult(data: ContentResponse) =
                localDataSource.updateContent(data.content.toString(), moduleId)
        }.asLiveData()
    }

    override fun setCourseBookmark(course: CourseEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setCourseBookmark(course, state) }

    override fun setReadModule(module: ModuleEntity) =
        appExecutors.diskIO().execute { localDataSource.setReadModule(module) }
}