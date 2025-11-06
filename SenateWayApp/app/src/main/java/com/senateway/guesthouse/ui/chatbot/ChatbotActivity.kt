package com.senateway.guesthouse.ui.chatbot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senateway.guesthouse.R
import com.senateway.guesthouse.databinding.ActivityChatbotBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// Represents a single chat message from either the user or the ai
data class Message(
    val id: Int,
    val text: String,
    val sender: String, // "user" or "bot"
    val timestamp: Long = System.currentTimeMillis()
)

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding
    private lateinit var adapter: ChatbotAdapter
    private lateinit var quickQuestionsAdapter: QuickQuestionsAdapter
    private val messages = mutableListOf<Message>() // hold conversation history
    private var messageId = 0 // assign unique id to each message

    // Predefined quick questions to help users start a chat
    private val quickQuestions = listOf(
        "What are your room rates?",
        "What facilities do you offer?",
        "How far from the airport?",
        "Do you have parking?",
        "What are check-in times?",
        "Is WiFi available?"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar and enable back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AI Assistant"
        
        setupRecyclerView()
        setupViews()
        
        // Add welcome message
        addMessage("Hello! I'm the Senate Way Guesthouse assistant. How can I help you today? Feel free to ask about our rooms, facilities, location, or anything else!", "bot")
    }

    // Initialize RecyclerView for chat messages
    private fun setupRecyclerView() {
        adapter = ChatbotAdapter(messages)
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // Keeps the latest message visible
        }
        binding.recyclerViewMessages.adapter = adapter
    }
    
    private fun setupViews() {
        binding.buttonSend.setOnClickListener {
            sendMessage()
        }
        // Allow sending message with keyboard action
        //TimothyW. “Sending Text Messages (SMS) through Custom Keyboard Extension.” Stack Overflow, 3 Oct. 2017, stackoverflow.com/questions/46536783/sending-text-messages-sms-through-custom-keyboard-extension.
        binding.editTextMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
        
        // Initialize quick questions
        setupQuickQuestions()
    }
    
    private fun setupQuickQuestions() {
        quickQuestionsAdapter = QuickQuestionsAdapter(quickQuestions) { question ->
            // When a quick question is clicked, send it as a message
            binding.editTextMessage.setText(question)
            sendMessage()
        }
        
        binding.recyclerViewQuickQuestions.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewQuickQuestions.adapter = quickQuestionsAdapter
    }
    // Handle sending of user messages and fetch bot response
    //Shah, Smit. “Sending Data from Bot to Client.” Stack Overflow, 22 July 2020, stackoverflow.com/questions/63040122/sending-data-from-bot-to-client.
    private fun sendMessage() {
        val messageText = binding.editTextMessage.text.toString().trim()
        if (messageText.isEmpty()) return

        // Add user message to chat
        addMessage(messageText, "user")
        binding.editTextMessage.setText("")
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonSend.isEnabled = false

        // Fetch bot reply asynchronously
        //Dan, Iris. “Asynchronous Response to Task Module Fetch.” Stack Overflow, 21 Sept. 2022, stackoverflow.com/questions/73794876/asynchronous-response-to-task-module-fetch.
        getBotResponse(messageText) { response ->
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.buttonSend.isEnabled = true
                addMessage(response, "bot")
            }
        }
    }
    // Add a message (from user or bot) to the chat list
   // “Send a Message Using the Google Chat API.” Google for Developers, 2025, developers.google.com/workspace/chat/create-messages.
    private fun addMessage(text: String, sender: String) {
        val message = Message(messageId++, text, sender)
        messages.add(message)
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerViewMessages.smoothScrollToPosition(messages.size - 1)
        
        // Hide quick questions after first user message
        if (sender == "user") {
            binding.layoutQuickQuestions.visibility = View.GONE
        }
    }
    // Fetches a response from the Gemini API or fallback rules
    //“API Reference | Google AI for Developers.” Google for Developers, ai.google.dev/api.
    private fun getBotResponse(userMessage: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // use Gemini API if configured correctly, useful for testing
                val apiKey = getGeminiApiKey()
                if (apiKey.isNotEmpty()) {
                    val response = callGeminiAPI(apiKey, userMessage)
                    withContext(Dispatchers.Main) {
                        callback(response)
                    }
                } else {
                    // Fallback to rule based responses if Gemini API is not configured correctly. also useful for testong
                    val response = getFallbackResponse(userMessage)
                    withContext(Dispatchers.Main) {
                        callback(response)
                    }
                }
            } catch (e: Exception) {
                // Graceful error handling if API call fails
                withContext(Dispatchers.Main) {
                    callback("I'm having trouble connecting right now. Please try again later or contact us directly at +27 82 927 8907 or vanessa141169@yahoo.com.")
                }
            }
        }
    }
    
    private fun getGeminiApiKey(): String {
        // Get from strings.xml, good for security
        return getString(R.string.gemini_api_key)
    }
    // Makes a POST request to Gemini API to generate a short guesthouse specific reply
    private fun callGeminiAPI(apiKey: String, userMessage: String): String {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=$apiKey")
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        // System default prompt for ai to define the tone and response type for the ai
        // “API Reference | Google AI for Developers.” Google for Developers, ai.google.dev/api.
        val prompt = """You are a helpful assistant for a guesthouse in Kimberley, South Africa.
            Answer briefly (<=120 words), friendly, and encourage contacting us for bookings.
            Facts: 10 rooms (R400–R1600), free WiFi, free parking, pool, check-in 2:00 PM, check-out 10:00 AM, near airport (6 km) and malls (1.9 km).
            Question: "$userMessage"
        """.trimIndent()

        // Build JSON body for Gemini API request
        // “API Reference | Google AI for Developers.” Google for Developers, ai.google.dev/api.
        val requestBody = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(org.json.JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(org.json.JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("generationConfig", org.json.JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 2048)
            })
        }
        // Send request to Gemini API
        connection.outputStream.use { it.write(requestBody.toString().toByteArray()) }
        // Parse response if request succeeded
        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val response = connection.inputStream.bufferedReader().readText()
            val jsonResponse = JSONObject(response)
            val candidates = jsonResponse.getJSONArray("candidates")
            if (candidates.length() > 0) {
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text")
                }
            }
        }
        // Fallback if API fails or returns empty
        return getFallbackResponse(userMessage)
    }
    // Predefined responses for when Gemini API isn’t available making the ai still useful even when down
    private fun getFallbackResponse(userMessage: String): String {
        val lowerMessage = userMessage.lowercase()
        return when {
            lowerMessage.contains("rate") || lowerMessage.contains("price") || lowerMessage.contains("cost") ->
                "Our room rates range from R400 to R1600 per night, depending on the room type. Standard single rooms start at R400, while our Family Suite is R1600. Would you like more details about a specific room type?"
            
            lowerMessage.contains("wifi") || lowerMessage.contains("internet") ->
                "Yes, we offer free high-speed WiFi throughout the property. All our rooms and common areas have excellent WiFi connectivity."
            
            lowerMessage.contains("parking") ->
                "Yes, we provide free private parking for all our guests. The parking area is on-site and secure."
            
            lowerMessage.contains("airport") || lowerMessage.contains("distance") ->
                "We're located about 6 km from Kimberley Airport, which is approximately a 10-minute drive. We're happy to help arrange transportation if needed."
            
            lowerMessage.contains("check-in") || lowerMessage.contains("check in") ->
                "Check-in time is 2:00 PM and check-out is 10:00 AM. If you need early check-in or late check-out, please contact us and we'll do our best to accommodate you."
            
            lowerMessage.contains("facilities") || lowerMessage.contains("amenities") ->
                "We offer free WiFi, free parking, a year-round outdoor swimming pool, shared kitchen, BBQ facilities, air conditioning, and modern bathrooms. Many rooms also have TVs, mini fridges, and coffee makers."
            
            lowerMessage.contains("pool") ->
                "Yes, we have a beautiful year-round outdoor swimming pool with a view. There's also a sun terrace where you can relax and enjoy the surroundings."
            
            lowerMessage.contains("room") || lowerMessage.contains("accommodation") ->
                "We have 10 comfortable rooms ranging from standard single rooms to family suites. All rooms have air conditioning, and most have private bathrooms. Would you like details about a specific room type?"

            // If no predefined response is available for the question asked by the user
            else ->
                "Thank you for your question! For more detailed information or to make a booking, please contact us at +27 82 927 8907 or vanessa141169@yahoo.com. We're here to help make your stay memorable!"
        }
    }
    // Handle toolbar nav
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
