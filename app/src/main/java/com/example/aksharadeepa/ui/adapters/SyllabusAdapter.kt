package com.example.aksharadeepa.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aksharadeepa.data.Chapter
import com.example.aksharadeepa.databinding.ItemChapterBinding

class SyllabusAdapter(
    private val onChapterChecked: (Chapter, Boolean) -> Unit,
    private val onQuizClicked: (Chapter) -> Unit
) : ListAdapter<Chapter, SyllabusAdapter.ChapterViewHolder>(ChapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChapterViewHolder(private val binding: ItemChapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: Chapter) {
            binding.tvChapterName.text = chapter.name
            binding.tvChapterSubject.text = chapter.subject
            
            // Remove listener temporarily to avoid trigger during binding
            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = chapter.isCompleted
            
            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                onChapterChecked(chapter, isChecked)
            }

            binding.btnQuiz.setOnClickListener {
                onQuizClicked(chapter)
            }
        }
    }

    class ChapterDiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem == newItem
        }
    }
}
