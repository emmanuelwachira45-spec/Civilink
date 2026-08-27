package com.example.civilink.data

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel : ViewModel() {

    private val repository = ReportRepository()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount.asStateFlow()

    init {
        loadReports()
    }

    /**
     * Loads reports from the repository.
     */
    fun loadReports() {
        _isLoading.value = true
        _error.value = null

        repository.getReports(
            onSuccess = { reports ->
                _reports.value = reports
                _isLoading.value = false
                updateNotificationCount(reports)
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
    }

    private fun updateNotificationCount(reports: List<Report>) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Real-time calculation based on report statuses
            val count = reports.count { 
                it.userId == currentUser.uid && 
                (it.status == "In Progress" || it.status == "Resolved") 
            }
            _notificationCount.value = count
        }
    }

    /**
     * Uploads an image and returns the URL.
     */
    fun uploadImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        repository.uploadImage(
            imageUri = uri,
            onSuccess = { url ->
                _isLoading.value = false
                onSuccess(url)
            },
            onError = { err ->
                _isLoading.value = false
                onError(err)
            }
        )
    }

    /**
     * Creates a new report.
     */
    fun createReport(
        report: Report,
        onSuccess: (Report) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        _error.value = null

        repository.createReport(
            report = report,
            onSuccess = { savedReport ->
                _isLoading.value = false
                _reports.value = listOf(savedReport) + _reports.value
                onSuccess(savedReport)
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
                onError(errorMessage)
            }
        )
    }

    /**
     * Updates the status of an existing report.
     */
    fun updateReportStatus(
        reportId: String,
        newStatus: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        repository.updateReportStatus(
            reportId = reportId,
            newStatus = newStatus,
            onSuccess = {
                _reports.value = _reports.value.map { report ->
                    if (report.id == reportId) {
                        report.copy(status = newStatus)
                    } else {
                        report
                    }
                }
                updateNotificationCount(_reports.value)
                onSuccess()
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                onError(errorMessage)
            }
        )
    }

    /**
     * Deletes a report.
     */
    fun deleteReport(
        reportId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        repository.deleteReport(
            reportId = reportId,
            onSuccess = {
                _isLoading.value = false
                _reports.value = _reports.value.filter { it.id != reportId }
                updateNotificationCount(_reports.value)
                onSuccess()
            },
            onError = { err ->
                _isLoading.value = false
                _error.value = err
                onError(err)
            }
        )
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _error.value = null
    }
}
