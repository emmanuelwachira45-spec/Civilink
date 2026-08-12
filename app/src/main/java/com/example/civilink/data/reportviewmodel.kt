package com.example.civilink.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel : ViewModel() {

    private val repository = ReportRepository()


    // ─────────────────────────────────────
    // Reports
    // ─────────────────────────────────────

    private val _reports =
        MutableStateFlow<List<Report>>(emptyList())

    val reports: StateFlow<List<Report>> =
        _reports.asStateFlow()


    // ─────────────────────────────────────
    // Loading
    // ─────────────────────────────────────

    private val _isLoading =
        MutableStateFlow(value = false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // ─────────────────────────────────────
    // Error
    // ─────────────────────────────────────

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // ─────────────────────────────────────
    // Load Reports
    // ─────────────────────────────────────

    fun loadReports() {

        _isLoading.value = true
        _error.value = null

        repository.getReports(
            onSuccess = { reports ->
                _reports.value = reports
                _isLoading.value = false
            },
        ) { errorMessage ->
            _error.value = errorMessage
            _isLoading.value = false
        }
    }


    // ─────────────────────────────────────
    // Update Status
    // ─────────────────────────────────────

    fun updateReportStatus(
        reportId: String,
        newStatus: String
    ) {

        repository.updateReportStatus(

            reportId = reportId,
            newStatus = newStatus,

            onSuccess = {

                // Update local UI immediately
                _reports.value =
                    _reports.value.map { report ->

                        if (report.id == reportId) {

                            report.copy(
                                status = newStatus
                            )

                        } else {

                            report
                        }
                    }
            },

            onError = { errorMessage ->

                _error.value = errorMessage
            }
        )
    }
}