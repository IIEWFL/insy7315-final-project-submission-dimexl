package com.senateway.guesthouse.ui.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.databinding.ItemChatbotMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatbotAdapter(
    private val messages: List<Message> // List of chat messages
) : RecyclerView.Adapter<ChatbotAdapter.MessageViewHolder>() {
    
    companion object {
        private const val TYPE_USER = 1 // Message from tjhe user
        private const val TYPE_BOT = 2 // Message from the bot
    }

    // Determines which layout type to use for each message
    //“API Reference | Google AI for Developers.” Google for Developers, ai.google.dev/api.
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == "user") TYPE_USER else TYPE_BOT
    }
    // Inflates the chat message layout and sets up the correct view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatbotMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding, viewType == TYPE_USER)
    }
    
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }
    
    override fun getItemCount() = messages.size
    // Handles binding message data to the correct side of the chat bubble
    //Android, in. “Stack Overflow.” Stack Overflow, 2024, stackoverflow.com/questions/78828250/build-the-ios-messages-chat-bubblewith-bottom-right-and-left-tail-in-android. Accessed 4 Nov. 2025.
    inner class MessageViewHolder(
        private val binding: ItemChatbotMessageBinding,
        private val isUser: Boolean // Indicates if this message is from the user
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: Message) {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = timeFormat.format(Date(message.timestamp))
            
            // Set visibility based on sender
            if (isUser) {
                // User message bubble
                binding.userMessageLayout.visibility = android.view.View.VISIBLE
                binding.botMessageLayout.visibility = android.view.View.GONE
                binding.textUserMessage.text = message.text
                binding.textUserTimestamp.text = timeString
            } else {
                // Ai message bubble
                binding.userMessageLayout.visibility = android.view.View.GONE
                binding.botMessageLayout.visibility = android.view.View.VISIBLE
                binding.textBotMessage.text = message.text
                binding.textBotTimestamp.text = timeString
            }
        }
    }
}

