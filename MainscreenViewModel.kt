package com.example.semestralka

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.semestralka.database.ProjectDatabaseDao
import com.example.semestralka.databinding.FragmentSummaryBinding
import kotlinx.coroutines.launch

class MainscreenViewModel(val database: ProjectDatabaseDao, application: Application) : AndroidViewModel(application) {
    private var projectsNames = MutableLiveData<List<String>>()

    var timeStarted = false
    private var handler = Handler(Looper.getMainLooper())
    lateinit var runnable: Runnable
    var seconds = 0
    val projects: MutableLiveData<List<String>> get() = projectsNames

    init{
        initializeProjects()
    }

    /**
     * Metoda, ktora spusti cas s oneskorenim 1000 milisec
     *
     * @param textView komponent TextView, ktora sa bude menit pri spusteni stopiek
     */
    fun startTimer(textView: TextView) {
        if (!timeStarted) {
            runnable = Runnable {
                seconds++
                textView.text = getTimeStringFromIntInSecond(seconds)
                handler.postDelayed(runnable, 1000)
                Log.i("TIMER", "Stale bezi")

            }
            handler.postDelayed(runnable, 1000)
            textView.text = getTimeStringFromIntInSecond(seconds)
            timeStarted = true
        }

    }

    /**
     * Metoda, ktora zastavi stopky
     */
    fun stopTimer() {
        if (this::runnable.isInitialized){
            handler.removeCallbacks(runnable)
            timeStarted = false
        }

    }


    /**
     * Metoda, ktora vrati data do Spinner komponentu na zobrazenie
     */
    fun fetchSpinnerData(): LiveData<List<String>> {
        viewModelScope.launch {
            projectsNames.value = database.getAllNames()
        }
        return projectsNames
    }

    /**
     * Metoda, ktora inicializuje projekty
     */
    private fun initializeProjects() {
        viewModelScope.launch {
            projectsNames.value = database.getAllNames()
        }
    }

    /**
     * Metoda, ktora restaruje stopky
     *
     * @param textView komponent TextView, ktory sa ma zmenit pri restarovani stopiek
     */
    fun resetTime(textView: TextView) {
        seconds = 0
        textView.text = getTimeStringFromIntInSecond(seconds)
    }

    /**
     * Metoda, ktora nahra nemerany cas na projekt, podla parametrov
     *
     * @param name Meno projektu
     * @param time Namerany cas vo formate String
     */
    fun updateProject(name: String, time: String) {
        viewModelScope.launch {
            var project = database.get(name)
            if (project != null) {
                val timeOnTimer = getTimeInSecondsFromString(time)
                project.workedHours += timeOnTimer
                database.update(project)
            }
        }
    }

}