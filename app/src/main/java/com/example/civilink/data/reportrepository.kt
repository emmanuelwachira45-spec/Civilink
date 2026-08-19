package com.example.civilink.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class ReportRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val reportsRef: DatabaseReference = database.getReference("reports")

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

    // Get all reports from Firebase
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

    // Create a new report in Firebase
    fun createReport(
        report: Report,
        onSuccess: (Report) -> Unit,
        onError: (String) -> Unit
    ) {
        val pushRef = reportsRef.push()
        val reportKey = pushRef.key
        
        if (reportKey == null) {
            onError("Could not generate report ID")
            return
        }

        val reportWithId = report.copy(id = reportKey)

        pushRef.setValue(reportWithId)
            .addOnSuccessListener {
                onSuccess(reportWithId)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to submit report")
            }
    }

    // Update report status
    fun updateReportStatus(
        reportId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (reportId.isEmpty()) {
            onError("Invalid report ID")
            return
        }

        reportsRef.child(reportId).child("status").setValue(newStatus)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to update status")
            }
    }
}
