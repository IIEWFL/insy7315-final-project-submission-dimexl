package com.senateway.guesthouse.ui.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.databinding.ItemQuickQuestionBinding

class QuickQuestionsAdapter(
    private val questions: List<String>,  // List of quick question strings to display
    private val onQuestionClick: (String) -> Unit
) : RecyclerView.Adapter<QuickQuestionsAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        // Inflate the XML layout for each question item
        val binding = ItemQuickQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        // Bind the question text and click action to the view
        holder.bind(questions[position])
    }

    override fun getItemCount() = questions.size // Number of questrins to display

    inner class QuestionViewHolder(
        private val binding: ItemQuickQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String) {
            binding.textQuestion.text = question
            binding.root.setOnClickListener {
                onQuestionClick(question)
            }
        }
    }
}

