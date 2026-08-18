package com.example.civilink.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReportRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reportsRef: DatabaseReference = database.getReference("reports")

    /**
     * Get all reports. 
     * Implementation removed as per request.
     */
    fun getReports(
        onSuccess: (List<Report>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Implementation logic removed
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
        val reportMap = mapOf<String, Any>(
            "id" to reportWithId.id,
            "userId" to reportWithId.userId,
            "title" to reportWithId.title,
            "category" to reportWithId.category,
            "location" to reportWithId.location,
            "description" to reportWithId.description,
            "status" to reportWithId.status,
            "timestamp" to reportWithId.timestamp
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
}
