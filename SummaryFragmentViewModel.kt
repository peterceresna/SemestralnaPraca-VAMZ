package com.example.semestralka

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.semestralka.database.ProjectDatabaseDao
import com.example.semestralka.database.ProjectEntity
import kotlinx.coroutines.launch

class SummaryFragmentViewModel(val database: ProjectDatabaseDao, application: Application): AndroidViewModel(application) {
    val projects = MutableLiveData<List<ProjectEntity?>>()

    //val projectsString =
    init{
        initializeProjects()
    }

    private fun initializeProjects() {
        viewModelScope.launch {
            projects.value = getProjectsFromDatabase()
        }
    }

    /**
     * Metoda, ktora vrati projekty z databazy
     */
    private suspend fun getProjectsFromDatabase(): List<ProjectEntity>? {
        val projects = database.getAllProjects()
        return projects
    }

    /**
     * Metoda, ktora vycisti databazu
     */
    fun clearDatabase() {
        viewModelScope.launch {
            database.clearDatabase()
        }
    }

}