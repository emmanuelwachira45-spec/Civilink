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
        MutableStateFlow(false)

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
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
    }


    // ─────────────────────────────────────
    // Create / Submit Report
    // ─────────────────────────────────────

    fun createReport(
        report: Report,
        onSuccess: () -> Unit = {}
    ) {

        _isLoading.value = true
        _error.value = null

        repository.createReport(
            report = report,
            onSuccess = { savedReport ->
                _isLoading.value = false
                // Add the actual Firebase report
                // including its generated ID
                _reports.value =
                    listOf(savedReport) + _reports.value
                onSuccess()
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
    }


    // ─────────────────────────────────────
    // Update Report Status
    // ─────────────────────────────────────

    fun updateReportStatus(
        reportId: String,
        newStatus: String
    ) {

        repository.updateReportStatus(
            reportId = reportId,
            newStatus = newStatus,
            onSuccess = {
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


    // ─────────────────────────────────────
    // Clear Error
    // ─────────────────────────────────────

    fun clearError() {

        _error.value = null
    }
}
