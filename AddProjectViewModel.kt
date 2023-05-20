package com.example.semestralka

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.semestralka.database.ProjectDatabaseDao
import com.example.semestralka.database.ProjectEntity
import kotlinx.coroutines.launch

class AddProjectViewModel(val database: ProjectDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var projects = MutableLiveData<List<ProjectEntity>?>()


    init {
        initializeProjects()
    }

    /**
     * Inicializuje projekty, kvoli kontrole
     */
    private fun initializeProjects() {
        viewModelScope.launch {
            projects.value = getProjectsFromDatabase()
        }
    }

    /**
     * Metoda, ktora prida novy projekt do databazy
     *
     * @param name Meno projektu
     * @param Hours Hodiny odporucane pre projekt
     */
    fun addProject(name: String, Hours: Int) {
        viewModelScope.launch {
            var project = ProjectEntity(name, 0, Hours)
            database.insert(project)
        }
    }

    /**
     * Metoda, ktora kontroluje ci uz existuje projekt s danym menom
     *
     * @param name Meno projektu
     */
    fun hasProject(name: String): Boolean {
        var list = projects.value
        for (project in list!!) {
            if (project.projectName.equals(name)) {
                return true
            }
        }
        return false
    }

    /**
     * Metoda, ktora vracia projekty z databazy
     */
    private suspend fun getProjectsFromDatabase(): List<ProjectEntity>? {
        var project = database.getAllProjects()
        return project
    }



}