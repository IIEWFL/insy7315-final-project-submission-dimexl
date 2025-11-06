package com.senateway.guesthouse.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import com.senateway.guesthouse.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 Represents an email attachment
 @param fileBytes The file content as ByteArray
 are_flour. “Sending an Email from an Android in Kotlin.” Stack Overflow, 12 June 2023, stackoverflow.com/questions/76459425/sending-an-email-from-an-android-in-kotlin.
 */
data class EmailAttachment(
    val fileBytes: ByteArray,
    val fileName: String,
    val mimeType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAttachment

        if (!fileBytes.contentEquals(other.fileBytes)) return false
        if (fileName != other.fileName) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileBytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

object EmailService {
    private const val TAG = "EmailService"
    private const val EMAILJS_API_URL = "https://api.emailjs.com/api/v1.0/email/send"
    

    private const val MAX_ATTACHMENT_SIZE_BYTES = 50 * 1024 // 50KB free plan

    // Single OkHttp client with timeout configuration
    //kelvincer. “How to Set Connection Timeout with OkHttp.” Stack Overflow, 20 Sept. 2014, stackoverflow.com/questions/25953819/how-to-set-connection-timeout-with-okhttp.
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private fun encodeAttachmentToBase64(attachment: EmailAttachment): String {
        return Base64.encodeToString(attachment.fileBytes, Base64.NO_WRAP)
    }

    private fun addAttachmentToParams(
        templateParams: JSONObject,
        attachment: EmailAttachment,
        attachmentIndex: Int = 0
    ) {
        val base64Content = encodeAttachmentToBase64(attachment)
        

        if (attachment.fileBytes.size > MAX_ATTACHMENT_SIZE_BYTES) {
            Log.w(TAG, "Attachment ${attachment.fileName} is ${attachment.fileBytes.size} bytes, " +
                    "which exceeds the recommended ${MAX_ATTACHMENT_SIZE_BYTES} bytes limit for free plan")
        }

        if (attachmentIndex == 0) {

            templateParams.put("attachment", base64Content)
            templateParams.put("attachment_name", attachment.fileName)
            templateParams.put("attachment_type", attachment.mimeType)
        } else {

            templateParams.put("attachment_$attachmentIndex", base64Content)
            templateParams.put("attachment_${attachmentIndex}_name", attachment.fileName)
            templateParams.put("attachment_${attachmentIndex}_type", attachment.mimeType)
        }
        
        Log.d(TAG, "Added attachment: ${attachment.fileName} (${attachment.fileBytes.size} bytes, type: ${attachment.mimeType})")
    }
    

     //Send booking received email to the guest
     //kelvincer. “How to Set Connection Timeout with OkHttp.” Stack Overflow, 20 Sept. 2014, stackoverflow.com/questions/25953819/how-to-set-connection-timeout-with-okhttp.
    suspend fun sendBookingReceivedEmail(
        context: Context,
        name: String,
        email: String,
        phone: String,
        checkIn: String,
        checkOut: String,
        guests: String,
        message: String,
        attachments: List<EmailAttachment> = emptyList()
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val serviceId = context.getString(R.string.emailjs_service_id)
            val templateId = context.getString(R.string.emailjs_template_booking_received)
            val publicKey = context.getString(R.string.emailjs_public_key)
            
            if (serviceId.isEmpty() || templateId.isEmpty() || publicKey.isEmpty()) {
                return@withContext Result.failure(Exception("EmailJS configuration is missing"))
            }
            
            val templateParams = JSONObject().apply {
                put("from_name", name)
                put("from_email", email)
                put("to_email", email)
                put("phone", phone)
                put("check_in", checkIn)
                put("check_out", checkOut)
                put("guests", guests)
                put("message", message)

                attachments.forEachIndexed { index, attachment ->
                    addAttachmentToParams(this, attachment, index)
                }
            }
            
            val requestBody = JSONObject().apply {
                put("service_id", serviceId)
                put("template_id", templateId)
                put("user_id", publicKey)
                put("template_params", templateParams)
            }.toString()
            
            Log.d(TAG, "Sending booking received email request: $requestBody")
            
            val request = Request.Builder()
                .url(EMAILJS_API_URL)
                .addHeader("Content-Type", "application/json")
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            Log.d(TAG, "EmailJS response code: ${response.code}, body: $responseBody")
            
            if (response.isSuccessful) {
                Log.d(TAG, "Booking received email sent successfully to $email")
                Result.success(Unit)
            } else {
                Log.e(TAG, "Failed to send booking received email. Code: ${response.code}, Body: $responseBody")
                Result.failure(Exception("Failed to send email: ${response.code} - $responseBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending booking received email", e)
            Result.failure(e)
        }
    }

     //Send booking confirmation email to the guest
     //kelvincer. “How to Set Connection Timeout with OkHttp.” Stack Overflow, 20 Sept. 2014, stackoverflow.com/questions/25953819/how-to-set-connection-timeout-with-okhttp.
    suspend fun sendBookingConfirmationEmail(
        context: Context,
        guestName: String,
        guestEmail: String,
        phone: String,
        checkIn: String,
        checkOut: String,
        guests: String,
        message: String = "",
        attachments: List<EmailAttachment> = emptyList()
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val serviceId = context.getString(R.string.emailjs_service_id)
            val templateId = context.getString(R.string.emailjs_template_booking_confirmed)
            val publicKey = context.getString(R.string.emailjs_public_key)
            
            if (serviceId.isEmpty() || templateId.isEmpty() || publicKey.isEmpty()) {
                return@withContext Result.failure(Exception("EmailJS configuration is missing"))
            }
            
            val templateParams = JSONObject().apply {
                put("from_name", "SenateWay Guesthouse")
                put("from_email", "vanessa141169@yahoo.com")
                put("to_email", guestEmail)
                put("to_name", guestName)
                put("guest_name", guestName)
                put("phone", phone)
                put("check_in", checkIn)
                put("check_out", checkOut)
                put("guests", guests)
                put("message", message)
                
                // Add attachments if provided
                attachments.forEachIndexed { index, attachment ->
                    addAttachmentToParams(this, attachment, index)
                }
            }
            
            val requestBody = JSONObject().apply {
                put("service_id", serviceId)
                put("template_id", templateId)
                put("user_id", publicKey)
                put("template_params", templateParams)
            }.toString()
            
            Log.d(TAG, "Sending booking confirmation email request: $requestBody")
            
            val request = Request.Builder()
                .url(EMAILJS_API_URL)
                .addHeader("Content-Type", "application/json")
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            Log.d(TAG, "EmailJS response code: ${response.code}, body: $responseBody")
            
            if (response.isSuccessful) {
                Log.d(TAG, "Booking confirmation email sent successfully to $guestEmail")
                Result.success(Unit)
            } else {
                Log.e(TAG, "Failed to send booking confirmation email. Code: ${response.code}, Body: $responseBody")
                Result.failure(Exception("Failed to send email: ${response.code} - $responseBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending booking confirmation email", e)
            Result.failure(e)
        }
    }
    

     //Helper function to create a PDF attachment
    fun createPdfAttachment(
        pdfBytes: ByteArray,
        fileName: String = "attachment.pdf"
    ): EmailAttachment {
        return EmailAttachment(
            fileBytes = pdfBytes,
            fileName = fileName,
            mimeType = "application/pdf"
        )
    }
}
