package com.example.civilink.data

import com.google.firebase.database.*

class ReportRepository {

    private val database =
        FirebaseDatabase.getInstance().getReference("reports")

    fun getReports(
        onSuccess: (List<Report>) -> Unit,
        onError: (String) -> Unit
    ) {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val reports = mutableListOf<Report>()

                for (child in snapshot.children) {

                    val report =
                        child.getValue(Report::class.java)

                    report?.let {
                        reports.add(it.copy(id = child.key ?: ""))
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