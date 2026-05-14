package com.example.aksharadeepa.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aksharadeepa.R
import com.example.aksharadeepa.data.DataProvider
import com.example.aksharadeepa.data.Question
import com.example.aksharadeepa.data.SharedPrefsManager
import com.example.aksharadeepa.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: SharedPrefsManager

    private var currentSubject: String = ""
    private var currentQuestions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private var timer: CountDownTimer? = null
    private val userAnswers = mutableListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsManager = SharedPrefsManager(requireContext())

        setupSelectionState()
        
        binding.btnStartQuiz.setOnClickListener {
            startQuiz()
        }

        binding.btnNext.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < currentQuestions.size) {
                showQuestion()
            } else {
                showResult()
            }
        }

        binding.btnRetake.setOnClickListener {
            setupSelectionState()
        }

        val options = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        for ((index, btn) in options.withIndex()) {
            btn.setOnClickListener {
                onOptionSelected(index)
            }
        }
    }

    private fun setupSelectionState() {
        binding.layoutSelection.visibility = View.VISIBLE
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResult.visibility = View.GONE

        val subjects = DataProvider.subjects.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, subjects)
        binding.spinnerSubject.adapter = adapter
    }

    private fun startQuiz() {
        currentSubject = binding.spinnerSubject.selectedItem.toString()
        currentQuestions = DataProvider.quizzes[currentSubject] ?: emptyList()
        currentQuestionIndex = 0
        score = 0
        userAnswers.clear()

        binding.layoutSelection.visibility = View.GONE
        binding.layoutQuiz.visibility = View.VISIBLE
        binding.layoutResult.visibility = View.GONE

        if (currentQuestions.isNotEmpty()) {
            showQuestion()
        }
    }

    private fun showQuestion() {
        val q = currentQuestions[currentQuestionIndex]
        binding.tvQuestionProgress.text = "Question ${currentQuestionIndex + 1}/${currentQuestions.size}"
        binding.tvQuestionText.text = q.text

        val options = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        for (i in options.indices) {
            options[i].text = q.options[i]
            options[i].isEnabled = true
            options[i].backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        }

        binding.btnNext.visibility = View.GONE
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        binding.progressTimer.progress = 30
        binding.tvTimerText.text = "30s remaining"

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                binding.progressTimer.progress = seconds
                binding.tvTimerText.text = "${seconds}s remaining"
            }

            override fun onFinish() {
                binding.progressTimer.progress = 0
                binding.tvTimerText.text = "Time's up!"
                onOptionSelected(-1) // Time up means wrong answer
            }
        }.start()
    }

    private fun onOptionSelected(selectedIndex: Int) {
        timer?.cancel()
        userAnswers.add(selectedIndex)

        val q = currentQuestions[currentQuestionIndex]
        val options = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        
        for (i in options.indices) {
            options[i].isEnabled = false
            if (i == q.correctAnswerIndex) {
                options[i].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.success_green))
            } else if (i == selectedIndex) {
                options[i].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.error_red))
            }
        }

        if (selectedIndex == q.correctAnswerIndex) {
            score++
        }

        binding.btnNext.visibility = View.VISIBLE
    }

    private fun showResult() {
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResult.visibility = View.VISIBLE

        binding.tvScore.text = "Score: $score/${currentQuestions.size}"
        prefsManager.saveQuizScore(currentSubject, score)

        val reviewBuilder = java.lang.StringBuilder()
        for (i in currentQuestions.indices) {
            val q = currentQuestions[i]
            val uAns = userAnswers.getOrNull(i) ?: -1
            reviewBuilder.append("Q${i + 1}: ${q.text}\n")
            reviewBuilder.append("Correct: ${q.options[q.correctAnswerIndex]}\n")
            if (uAns == q.correctAnswerIndex) {
                reviewBuilder.append("Your Answer: Correct\n\n")
            } else {
                val ansStr = if (uAns >= 0) q.options[uAns] else "Time Up / No Answer"
                reviewBuilder.append("Your Answer: $ansStr\n\n")
            }
        }
        binding.tvReview.text = reviewBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }
}
