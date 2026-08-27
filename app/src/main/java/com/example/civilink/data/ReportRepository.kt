package com.example.civilink.data

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ReportRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val reportsRef: DatabaseReference = database.getReference("reports")

    /**
     * Uploads an image to Firebase Storage.
     */
    fun uploadImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val fileName = "reports/${System.currentTimeMillis()}.jpg"
        val storageRef = storage.reference.child(fileName)

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener {
                onError(it.message ?: "Image upload failed")
            }
    }

    /**
     * Get all reports from Firebase.
     */
    fun getReports(
        onSuccess: (List<Report>) -> Unit,
        onError: (String) -> Unit
    ) {
        reportsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reportsList = mutableListOf<Report>()
                
                for (reportSnapshot in snapshot.children) {
                    try {
                        val report = reportSnapshot.getValue(Report::class.java)
                        if (report != null) {
                            // Ensure ID matches the Firebase key
                            val id = reportSnapshot.key ?: ""
                            reportsList.add(report.copy(id = id))
                        }
                    } catch (e: Exception) {
                        // Skip reports that fail to parse
                    }
                }
                
                // Sort by timestamp descending
                reportsList.sortByDescending { it.timestamp }
                onSuccess(reportsList)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    /**
     * Creates a new report in Firebase.
     */
    fun createReport(
        report: Report,
        onSuccess: (Report) -> Unit,
        onError: (String) -> Unit
    ) {
        val pushRef = reportsRef.push()
        val reportKey = pushRef.key?.toString()
        
        if (reportKey == null) {
            onError("Failed to generate report ID")
            return
        }

        val reportWithId = report.copy(id = reportKey)

        // Convert to Map to bypass analyzer/resolution issues with data classes
        val reportMap = mapOf<String, Any?>(
            "id" to reportWithId.id,
            "userId" to reportWithId.userId,
            "title" to reportWithId.title,
            "category" to reportWithId.category,
            "location" to reportWithId.location,
            "description" to reportWithId.description,
            "status" to reportWithId.status,
            "timestamp" to reportWithId.timestamp,
            "imageUrl" to reportWithId.imageUrl
        )

        pushRef.setValue(reportMap)
            .addOnSuccessListener {
                onSuccess(reportWithId)
            }
            .addOnFailureListener { exception ->
                val message = exception.message?.toString() ?: "Submission failed"
                onError(message)
            }
    }

    /**
     * Updates the status of an existing report.
     */
    fun updateReportStatus(
        reportId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (reportId.isEmpty()) {
            onError("Invalid ID")
            return
        }

        reportsRef.child(reportId).child("status").setValue(newStatus as Any)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                val message = exception.message?.toString() ?: "Update failed"
                onError(message)
            }
    }

    /**
     * Deletes a report from Firebase.
     */
    fun deleteReport(
        reportId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        reportsRef.child(reportId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Deletion failed") }
    }
}
