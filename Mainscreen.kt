package com.example.semestralka


import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.semestralka.database.ProjectDatabase
import com.example.semestralka.databinding.FragmentMainscreenBinding

class Mainscreen : Fragment() {


    private val viewModel: MainscreenViewModel by viewModels()
    private lateinit var binding: FragmentMainscreenBinding
    lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mainscreen, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Hlavna obrazovka"
        application = requireNotNull(this.activity).application
        val dataSource = ProjectDatabase.getInstance(application).projectDatabaseDao
        val viewModelFactory = MainscreenViewModelFactory(dataSource, application)
        val mainscreenViewModel = ViewModelProvider(this, viewModelFactory).get(MainscreenViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.mainscreenViewModel = mainscreenViewModel
        val spinner = binding.projectSpinner

        //Nastavenie Observera na Spinner
        viewModel.fetchSpinnerData().observe(viewLifecycleOwner, Observer { spinnerData->
            val spinnerAdapter = ArrayAdapter(application, android.R.layout.simple_spinner_item, spinnerData)
            spinner.adapter = spinnerAdapter
        })
        //nacitanie ulozeneho stavu
        if (savedInstanceState != null) {
            binding.timeElapsedText.text = savedInstanceState.getString("TIME_ELAPSED")
            viewModel.timeStarted = savedInstanceState.getBoolean("Timerrunning")
            if (viewModel.timeStarted) {
                viewModel.timeStarted = false
                viewModel.startTimer(binding.timeElapsedText)
            }
        }

        binding.startButton.setOnClickListener{startTimer()
        }
        binding.stopButton.setOnClickListener{stopTimer()}
        binding.resetTimerButton.setOnClickListener{resetTimer()}
        binding.updateTimeButton.setOnClickListener{updateProjectTime()}

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Nastavenie Listenero na komponenty
        binding.addProjectButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_mainscreen_to_addProject3)
        }

        binding.showTimeCheckBox.setOnClickListener {
            if (binding.showTimeCheckBox.isChecked) {
                binding.timeElapsedLabel.visibility = View.VISIBLE
                binding.timeElapsedText.visibility = View.VISIBLE
            } else {
                binding.timeElapsedLabel.visibility = View.INVISIBLE
                binding.timeElapsedText.visibility = View.INVISIBLE
            }
        }

        binding.navToSummaryButton.setOnClickListener{view : View ->
            if (!viewModel.timeStarted) {
                view.findNavController().navigate(R.id.action_mainscreen_to_summaryFragment)
            }
        }
    }
    //Ulozenie stavu napr. pri rotacii
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("TIME_ELAPSED", binding.timeElapsedText.text.toString())
        outState.putBoolean("Timerrunning", viewModel.timeStarted)
    }

    //Zastavenie stopiek , kvoli rotacii zariadenia
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }

    /**
     * Metoda, ktora spusti stopky v pripade, ze spinner obsahuje hodnoty
     */
    private fun startTimer() {
        var spinner = binding.projectSpinner
        if (spinner.selectedItem != null) {
            spinner.setEnabled(false)
            Toast.makeText(application, "Stopky spustene", Toast.LENGTH_SHORT).show()
            viewModel.startTimer(binding.timeElapsedText)
        } else {
            Toast.makeText(application, "Pridajte projekt", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * Metoda, ktora restartuje stopky
     */
    private fun resetTimer() {
        viewModel.resetTime(binding.timeElapsedText)
    }

    /**
     * Metoda, ktora zastavi stopky
     */
    private fun stopTimer() {
        viewModel.stopTimer()
        Toast.makeText(application, "Stopky zastavene", Toast.LENGTH_SHORT).show()
    }

    /**
     * Metooda, ktora nahra namerany cas k projektu
     */
    private fun updateProjectTime() {
        val item = binding.projectSpinner.selectedItem
        if (item != null) {
            viewModel.stopTimer()
            viewModel.updateProject(
                binding.projectSpinner.selectedItem.toString(),
                binding.timeElapsedText.text.toString()
            )
            viewModel.resetTime(binding.timeElapsedText)
        } else {
            Toast.makeText(application, "Cas nie je mozne nahrat", Toast.LENGTH_LONG).show()
        }
    }

}