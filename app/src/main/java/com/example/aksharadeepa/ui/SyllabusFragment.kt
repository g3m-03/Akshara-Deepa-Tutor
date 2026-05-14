package com.example.aksharadeepa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aksharadeepa.R
import com.example.aksharadeepa.data.DataProvider
import com.example.aksharadeepa.data.SharedPrefsManager
import com.example.aksharadeepa.databinding.FragmentSyllabusBinding

class SyllabusFragment : Fragment() {

    private var _binding: FragmentSyllabusBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: SharedPrefsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSyllabusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsManager = SharedPrefsManager(requireContext())

        val adapter = SyllabusExpandableListAdapter(prefsManager)
        binding.expandableListView.setAdapter(adapter)
        
        binding.expandableListView.setOnGroupClickListener { parent, v, groupPosition, id ->
            val ivIndicator = v.findViewById<ImageView>(R.id.ivGroupIndicator)
            if (parent.isGroupExpanded(groupPosition)) {
                ivIndicator.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                ivIndicator.setImageResource(android.R.drawable.arrow_up_float)
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class SyllabusExpandableListAdapter(private val prefs: SharedPrefsManager) : BaseExpandableListAdapter() {

        private val subjects = DataProvider.subjects

        override fun getGroupCount(): Int = subjects.size

        override fun getChildrenCount(groupPosition: Int): Int = subjects[groupPosition].chapters.size

        override fun getGroup(groupPosition: Int): Any = subjects[groupPosition]

        override fun getChild(groupPosition: Int, childPosition: Int): Any = subjects[groupPosition].chapters[childPosition]

        override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

        override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

        override fun hasStableIds(): Boolean = true

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_syllabus_group, parent, false)
            val tvGroupName = view.findViewById<TextView>(R.id.tvGroupName)
            val ivIndicator = view.findViewById<ImageView>(R.id.ivGroupIndicator)

            val subject = getGroup(groupPosition) as com.example.aksharadeepa.data.Subject
            tvGroupName.text = subject.name
            
            if (isExpanded) {
                ivIndicator.setImageResource(android.R.drawable.arrow_up_float)
            } else {
                ivIndicator.setImageResource(android.R.drawable.arrow_down_float)
            }

            return view
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_syllabus_child, parent, false)
            val cbChapter = view.findViewById<CheckBox>(R.id.cbChapter)
            val tvChapterName = view.findViewById<TextView>(R.id.tvChapterName)

            val subject = getGroup(groupPosition) as com.example.aksharadeepa.data.Subject
            val chapterName = getChild(groupPosition, childPosition) as String

            tvChapterName.text = chapterName
            
            // Remove listener temporarily to avoid false triggers
            cbChapter.setOnCheckedChangeListener(null)
            cbChapter.isChecked = prefs.isChapterCompleted(subject.name, chapterName)
            
            cbChapter.setOnCheckedChangeListener { _, isChecked ->
                prefs.setChapterCompleted(subject.name, chapterName, isChecked)
            }

            // Also make clicking the row toggle the checkbox
            view.setOnClickListener {
                cbChapter.isChecked = !cbChapter.isChecked
            }

            return view
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
    }
}
