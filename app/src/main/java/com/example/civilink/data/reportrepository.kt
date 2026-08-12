package com.example.civilink.data

import com.google.firebase.database.*

class ReportRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reportsRef = database.getReference("reports")

    // Read all reports
    fun getReports(
        onSuccess: (List<Report>) -> Unit,
        onError: (String) -> Unit,
    ) {

        reportsRef.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val reports = mutableListOf<Report>()

                    for (reportSnapshot in snapshot.children) {

                        reportSnapshot.getValue(Report::class.java)?.let { report ->
                            reports.add(
                                report.copy(
                                    id = reportSnapshot.key ?: ""
                                )
                            )
                        }
                    }

                    // Newest reports first
                    reports.sortByDescending {
                        it.timestamp
                    }

                    onSuccess(reports)
                }

                override fun onCancelled(error: DatabaseError) {

                    onError(error.message)
                }
            }
        )
    }


    // Update report status
    fun updateReportStatus(
        reportId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        reportsRef
            .child(reportId)
            .child("status")
            .setValue(newStatus)
            .addOnSuccessListener {

                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message ?: "Failed to update status"
                )
            }
    }
}