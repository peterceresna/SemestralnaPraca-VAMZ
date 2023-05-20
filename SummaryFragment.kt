package com.example.semestralka

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.semestralka.database.ProjectDatabase
import com.example.semestralka.database.ProjectEntity
import com.example.semestralka.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {
    lateinit var binding: FragmentSummaryBinding
    private val viewModel: SummaryFragmentViewModel by viewModels()
    private lateinit var application: Application
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentSummaryBinding>(inflater,
            R.layout.fragment_summary, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Prehlad projektov"
        application = requireNotNull(this.activity).application
        val dataSource = ProjectDatabase.getInstance(application).projectDatabaseDao
        val viewModelFactory = SummaryFragmentViewModelFactory(dataSource, application)
        val summaryProjectViewModel = ViewModelProvider(this, viewModelFactory).get(
            SummaryFragmentViewModel::class.java)

        binding.summaryFragmentViewModel = summaryProjectViewModel

        val adapter = ProjectItemAdapter()
        binding.summaryList.adapter = adapter

        summaryProjectViewModel.projects.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("SummaryFragment", it.toString())
                adapter.data = it as List<ProjectEntity>
            }
        })
        binding.setLifecycleOwner(this)
        //return inflater.inflate(R.layout.fragment_summary, container, false)
        binding.clearDatabaseButton.setOnClickListener{view : View ->
            onClearDatabase()
            view.findNavController().navigate(R.id.action_summaryFragment_to_mainscreen)

        }
        return binding.root
    }



    /**
     * Metoda, ktora vycisti celu databazu
     */
    private fun onClearDatabase() {
        viewModel.clearDatabase()
        Toast.makeText(application, "Projekty z databazy vymazane", Toast.LENGTH_LONG).show()
    }
}