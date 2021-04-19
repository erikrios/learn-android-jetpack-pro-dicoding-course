package com.erikriosetiawan.academy.di

import android.content.Context
import com.erikriosetiawan.academy.data.AcademyRepository
import com.erikriosetiawan.academy.data.source.local.LocalDataSource
import com.erikriosetiawan.academy.data.source.local.room.AcademyDatabase
import com.erikriosetiawan.academy.data.source.remote.RemoteDataSource
import com.erikriosetiawan.academy.utils.AppExecutors
import com.erikriosetiawan.academy.utils.JsonHelper

object Injection {
    fun provideRepository(context: Context): AcademyRepository {

        val database = AcademyDatabase.getInstance(context)


        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(database.academyDao())
        val appExecutors = AppExecutors()
        return AcademyRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}