package com.example.semestralka

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semestralka.database.ProjectDatabaseDao

class SummaryFragmentViewModelFactory(private val dataSource: ProjectDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun<T: ViewModel> create(modelClass: Class<T>): T{
            if (modelClass.isAssignableFrom(SummaryFragmentViewModel::class.java)) {
                return SummaryFragmentViewModel(dataSource, application) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
        }

}