package com.example.aksharadeepa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aksharadeepa.R
import com.example.aksharadeepa.data.SharedPrefsManager
import com.example.aksharadeepa.databinding.FragmentHomeBinding
import com.example.aksharadeepa.databinding.ItemSubjectHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: SharedPrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsManager = SharedPrefsManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val overallProgress = prefsManager.getOverallProgress()
        binding.progressOverall.progress = overallProgress
        binding.tvOverallProgress.text = "$overallProgress%"

        val isActiveToday = prefsManager.isActiveToday()
        if (isActiveToday) {
            binding.cardDailyGoal.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success_green))
            binding.cardDailyGoal.findViewById<TextView>(R.id.tvDailyGoalTitle).text = "Daily Goal: Achieved!"
            binding.cardDailyGoal.findViewById<TextView>(R.id.tvDailyGoalDesc).text = "Great job! You've made progress today."
        } else {
            binding.cardDailyGoal.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.warning_orange))
            binding.cardDailyGoal.findViewById<TextView>(R.id.tvDailyGoalTitle).text = "Daily Goal: Incomplete"
            binding.cardDailyGoal.findViewById<TextView>(R.id.tvDailyGoalDesc).text = "Complete a chapter or take a quiz to meet your daily goal!"
        }

        setupSubjectCard(binding.cardScience.root, "Science")
        setupSubjectCard(binding.cardMath.root, "Mathematics")
        setupSubjectCard(binding.cardSocial.root, "Social Studies")
    }

    private fun setupSubjectCard(view: View, subjectName: String) {
        val bind = ItemSubjectHomeBinding.bind(view)
        bind.tvSubjectName.text = subjectName
        val progress = prefsManager.getSubjectProgress(subjectName)
        bind.progressSubject.progress = progress
        bind.tvSubjectProgress.text = "$progress%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
