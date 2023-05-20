package com.example.semestralka

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.semestralka.database.ProjectDatabase
import com.example.semestralka.databinding.FragmentAddProjectBinding
import kotlinx.coroutines.launch


class AddProject : Fragment() {

    private val viewModel: AddProjectViewModel by viewModels()
    private lateinit var binding: FragmentAddProjectBinding
    private var projectAdded = false
    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_project, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Pridanie projektu"
        application = requireNotNull(this.activity).application

        val dataSource = ProjectDatabase.getInstance(application).projectDatabaseDao

        val viewModelFactory = AddProjectViewModelFactory(dataSource, application)
        val addProjectViewModel = ViewModelProvider(this, viewModelFactory).get(AddProjectViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.addProjectViewModel = addProjectViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("AddProject", "onViewCreated called")
        binding.addProjectButton.setOnClickListener{view: View ->
            onAddProject()
            if (projectAdded) {
                view.findNavController().navigate(R.id.action_addProject3_to_mainscreen)
            }
        }
        //binding.ProjectNameTextField.text = getString(R.string.app_name, 0)
    }


    /**
     * Metoda, ktora prida projekt do databazy, zoberie hodnoty z komponentov a vlozi
     */
    private fun onAddProject() {
            val name = binding.ProjectNameTextField.text.toString()
            val hours = binding.ProjectHoursTextField.text.toString().toInt()
            if (viewModel.hasProject(name)) {
                Toast.makeText(application, "Projekt uz existuje", Toast.LENGTH_LONG).show()
                binding.ProjectNameTextField.error = "Projekt s nazvom existuje"
                binding.ProjectNameTextField.clearFocus()
                projectAdded = false
            } else {
                projectAdded = true
                viewModel.addProject(name, hours)
                Toast.makeText(application, "Projekt " + name + " bol pridany", Toast.LENGTH_LONG).show()
            }


    }
}