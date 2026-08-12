package com.example.civilink.data

import com.google.firebase.database.*

class Reportrepository {

    private val database = FirebaseDatabase.getInstance()
    private val reportsRef = database.getReference("reports")

    fun getReports(
        onSuccess: (List<Report>) -> Unit,
        onError: (String) -> Unit
    ) {
        reportsRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val reports = mutableListOf<Report>()

                for (reportSnapshot in snapshot.children) {

                    val report = reportSnapshot.getValue(Report::class.java)

                    if (report != null) {
                        reports.add(report.copy(id = reportSnapshot.key ?: ""))
                    }
                }

                onSuccess(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}