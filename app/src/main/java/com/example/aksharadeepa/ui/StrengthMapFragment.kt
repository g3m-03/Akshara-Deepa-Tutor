package com.example.aksharadeepa.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aksharadeepa.R
import com.example.aksharadeepa.data.SharedPrefsManager
import com.example.aksharadeepa.databinding.FragmentStrengthMapBinding
import com.example.aksharadeepa.databinding.ItemMasteryCardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StrengthMapFragment : Fragment() {

    private var _binding: FragmentStrengthMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: SharedPrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStrengthMapBinding.inflate(inflater, container, false)
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
        val sciScore = prefsManager.getAverageQuizScore("Science")
        val mathScore = prefsManager.getAverageQuizScore("Mathematics")
        val socScore = prefsManager.getAverageQuizScore("Social Studies")

        setupRadarChart(sciScore, mathScore, socScore)

        setupMasteryCard(binding.cardScienceMastery.root, "Science", sciScore)
        setupMasteryCard(binding.cardMathMastery.root, "Mathematics", mathScore)
        setupMasteryCard(binding.cardSocialMastery.root, "Social Studies", socScore)
    }

    private fun setupRadarChart(sci: Float, math: Float, soc: Float) {
        val entries = ArrayList<RadarEntry>()
        entries.add(RadarEntry(sci))
        entries.add(RadarEntry(math))
        entries.add(RadarEntry(soc))

        val dataSet = RadarDataSet(entries, "Mastery")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.primary)
        dataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.primary)
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 180
        dataSet.lineWidth = 2f
        dataSet.isDrawHighlightCircleEnabled = true
        dataSet.setDrawHighlightIndicators(false)

        val radarData = RadarData(dataSet)
        radarData.setValueTextSize(10f)
        radarData.setDrawValues(false)

        binding.radarChart.data = radarData
        binding.radarChart.description.isEnabled = false
        binding.radarChart.webLineWidth = 1f
        binding.radarChart.webColor = Color.LTGRAY
        binding.radarChart.webLineWidthInner = 1f
        binding.radarChart.webColorInner = Color.LTGRAY
        binding.radarChart.webAlpha = 100

        val xAxis = binding.radarChart.xAxis
        xAxis.textSize = 12f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Science", "Math", "Social"))
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)

        val yAxis = binding.radarChart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 10f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f
        yAxis.setDrawLabels(false)

        binding.radarChart.legend.isEnabled = false
        binding.radarChart.invalidate()
    }

    private fun setupMasteryCard(view: View, subjectName: String, score: Float) {
        val bind = ItemMasteryCardBinding.bind(view)
        bind.tvSubjectName.text = subjectName
        bind.tvMasteryPercent.text = "${score.toInt()}%"

        val tagText: String
        val tagColor: Int

        if (score > 70) {
            tagText = "Strong"
            tagColor = R.color.success_green
        } else if (score >= 40) {
            tagText = "Improving"
            tagColor = R.color.warning_orange
        } else {
            tagText = "Needs Focus"
            tagColor = R.color.error_red
        }

        bind.tvTag.text = tagText
        bind.cardTag.setCardBackgroundColor(ContextCompat.getColor(requireContext(), tagColor))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
