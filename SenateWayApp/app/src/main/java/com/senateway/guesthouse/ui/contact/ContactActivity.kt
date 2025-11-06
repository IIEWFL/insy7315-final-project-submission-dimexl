package com.senateway.guesthouse.ui.contact

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.senateway.guesthouse.config.FirebaseConfig
import com.senateway.guesthouse.databinding.ActivityContactBinding
import com.senateway.guesthouse.utils.EmailService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ContactActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityContactBinding
    private val calendar = Calendar.getInstance() // Date pickers (check in check ous)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViews() // Setup click listeners and date pickers
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Contact Us"
    }
    
    private fun setupViews() {
        binding.buttonSubmit.setOnClickListener {
            submitForm() // Handle form submission
        }
        
        // Setup date pickers
        binding.editTextCheckIn.apply {
            setKeyListener(null)
            setOnClickListener {
                showDatePicker(this)
                clearFocus()
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(this)
                    clearFocus()
                }
            }
        }
        
        binding.editTextCheckOut.apply {
            setKeyListener(null)
            setOnClickListener {
                showDatePicker(this)
                clearFocus()
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker(this)
                    clearFocus()
                }
            }
        }
    }
    // Method to show the date oickers
    private fun showDatePicker(editText: com.google.android.material.textfield.TextInputEditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        // Get the appropriate theme based on current mode
        val isDarkMode = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        val dialogTheme = com.senateway.guesthouse.R.style.DatePickerDialogTheme
        // Custom themed date picker dialog
        val dialog = DatePickerDialog(
            this,
            dialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        

        dialog.setOnShowListener {

            dialog.datePicker.postDelayed({
                styleCalendarView(dialog.datePicker, isDarkMode)
            }, 100)
        }
        
        dialog.show()
    }
    
    private fun styleCalendarView(datePicker: android.widget.DatePicker, isDarkMode: Boolean) {
        try {
            // Get the day text color
            //Alex. “Apply Style Only to Specific TextViews.” Stack Overflow, 14 May 2014, stackoverflow.com/questions/23656669/apply-style-only-to-specific-textviews.
            val dayTextColor = resources.getColor(
                com.senateway.guesthouse.R.color.datepicker_day_text_color,
                null
            )
            val selectedBgColor = resources.getColor(
                com.senateway.guesthouse.R.color.datepicker_selected_day_background,
                null
            )
            
            // Access CalendarView inside DatePicker
            val calendarView = datePicker.calendarView
            
            if (calendarView != null) {

                val calendarViewClass = calendarView.javaClass
                

                val children = getAllChildren(calendarView)
                for (child in children) {
                    if (child is android.widget.TextView) {
                        // Style day number text views so its more visible when chaning themes
                        //Alex. “Apply Style Only to Specific TextViews.” Stack Overflow, 14 May 2014, stackoverflow.com/questions/23656669/apply-style-only-to-specific-textviews.
                        child.setTextColor(dayTextColor)
                    } else if (child is android.view.ViewGroup) {
                        // Recursively style nested views
                        styleChildViews(child, dayTextColor)
                    }
                }
                

                try {
                    val method = calendarViewClass.getMethod("setSelectedWeekBackgroundColor", Int::class.java)
                    method.invoke(calendarView, selectedBgColor)
                } catch (e: Exception) {

                    try {
                        val method = calendarViewClass.getMethod("setSelectedDateVerticalBar", android.graphics.drawable.Drawable::class.java)
                        val drawable = android.graphics.drawable.ColorDrawable(selectedBgColor)
                        method.invoke(calendarView, drawable)
                    } catch (e2: Exception) {
                        android.util.Log.d("ContactActivity", "Could not set selected background: ${e2.message}")
                    }
                }
            }
            //Log used to debug this featurew
        } catch (e: Exception) {
            android.util.Log.e("ContactActivity", "Error styling calendar: ${e.message}", e)
        }
    }
    
    private fun getAllChildren(parent: android.view.View): List<android.view.View> {
        val children = mutableListOf<android.view.View>()
        if (parent is android.view.ViewGroup) {
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                children.add(child)
                children.addAll(getAllChildren(child))
            }
        }
        return children
    }
    
    private fun styleChildViews(parent: android.view.ViewGroup, textColor: Int) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is android.widget.TextView) {
                child.setTextColor(textColor)
            } else if (child is android.view.ViewGroup) {
                styleChildViews(child, textColor)
            }
        }
    }
    // Submit form to Firebase Realtime Database
    private fun submitForm() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val phone = binding.editTextPhone.text.toString().trim()
        val checkIn = binding.editTextCheckIn.text.toString().trim()
        val checkOut = binding.editTextCheckOut.text.toString().trim()
        val guests = binding.editTextGuests.text.toString().trim()
        val message = binding.editTextMessage.text.toString().trim()
        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            checkIn.isEmpty() || checkOut.isEmpty() || guests.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }
        // Create booking data object for Firebase
        //“Read and Write Data on the Web  |  Firebase Realtime Database.” Firebase, 2020, firebase.google.com/docs/database/web/read-and-write.
        val bookingData = hashMapOf<String, Any>(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "checkIn" to checkIn,
            "checkOut" to checkOut,
            "guests" to (guests.toIntOrNull() ?: 1),
            "message" to message,
            "status" to "pending", // until changed by admin
            "timestamp" to Date().toString(),
            "created" to System.currentTimeMillis()
        )
        // Push booking to Firebase Realtime Database
        //“Read and Write Data on the Web  |  Firebase Realtime Database.” Firebase, 2020, firebase.google.com/docs/database/web/read-and-write.
        FirebaseConfig.database.reference.child("bookings").push()
            .setValue(bookingData)
            .addOnSuccessListener {
                // Send booking received email
                //“Add Asynchronous Bookings.” Google for Developers, 2025, developers.google.com/actions-center/verticals/reservations/e2e/add-ons/add-async. Accessed 4 Nov. 2025.
                CoroutineScope(Dispatchers.Main).launch {
                    val emailResult = EmailService.sendBookingReceivedEmail(
                        context = this@ContactActivity,
                        name = name,
                        email = email,
                        phone = phone,
                        checkIn = checkIn,
                        checkOut = checkOut,
                        guests = guests,
                        message = message
                    )
                    
                    if (emailResult.isSuccess) {
                        Toast.makeText(this@ContactActivity, "Thank you for your booking request! We will contact you shortly.", Toast.LENGTH_LONG).show()
                    } else {
                        // Booking saved but email failed - show error for debugging
                        val errorMsg = emailResult.exceptionOrNull()?.message ?: "Unknown error"
                        android.util.Log.e("ContactActivity", "Email send failed: $errorMsg")
                        Toast.makeText(this@ContactActivity, "Booking saved! Email notification failed: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                    clearForm() // Resest the form after submission
                }
            }
            // Toast used for debugging
            .addOnFailureListener {
                Toast.makeText(this, "Error sending message. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        // Clear all the input fields
        binding.editTextName.setText("")
        binding.editTextEmail.setText("")
        binding.editTextPhone.setText("")
        binding.editTextCheckIn.setText("")
        binding.editTextCheckOut.setText("")
        binding.editTextGuests.setText("")
        binding.editTextMessage.setText("")
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

