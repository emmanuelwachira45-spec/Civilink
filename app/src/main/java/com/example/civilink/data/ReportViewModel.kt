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

    private var isListening = false

    init {
        loadReports()
    }

    /**
     * Loads reports from the repository.
     */
    fun loadReports() {
        if (isListening) return
        isListening = true

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
                isListening = false // Allow retry on error
            }
        )
    }

    private fun updateNotificationCount(reports: List<Report>) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Every report a citizen has filed appears in their
            // notification feed (including freshly submitted ones),
            // so the badge mirrors the full feed.
            val count = reports.count { it.userId == currentUser.uid }
            _notificationCount.value = count
        }
    }

    /**
     * Uploads a compressed image and returns the URL.
     */
    fun uploadImage(
        context: android.content.Context,
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        
        // Run compression on a background thread for UI performance
        val compressedBytes = ImageUtils.compressImage(context, uri)
        
        if (compressedBytes == null) {
            _isLoading.value = false
            onError("Failed to process image")
            return
        }

        repository.uploadImage(
            imageBytes = compressedBytes,
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
                        report.copy(
                            status = newStatus,
                            lastUpdatedAt = System.currentTimeMillis()
                        )
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
     * Edits an existing report's content (and optionally its status).
     * Powers the admin edit dialog – writes through to Firebase and
     * keeps every in-memory copy across screens in sync.
     */
    fun updateReport(
        reportId: String,
        title: String,
        category: String,
        location: String,
        description: String,
        status: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val updatedStamp = System.currentTimeMillis()

        repository.updateReport(
            reportId = reportId,
            updates = mapOf(
                "title" to title.trim(),
                "category" to category,
                "location" to location.trim(),
                "description" to description.trim(),
                "status" to status,
                "lastUpdatedAt" to updatedStamp
            ),
            onSuccess = {
                _reports.value = _reports.value.map { report ->
                    if (report.id == reportId) {
                        report.copy(
                            title = title.trim(),
                            category = category,
                            location = location.trim(),
                            description = description.trim(),
                            status = status,
                            lastUpdatedAt = updatedStamp
                        )
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
     * Lets a citizen verify if a report resolution is true or not.
     */
    fun verifyReportResolution(
        reportId: String,
        isTrue: Boolean,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val status = if (isTrue) "Verified" else "Disputed"
        repository.updateReport(
            reportId = reportId,
            updates = mapOf(
                "verificationStatus" to status,
                "lastUpdatedAt" to System.currentTimeMillis()
            ),
            onSuccess = {
                _reports.value = _reports.value.map { report ->
                    if (report.id == reportId) {
                        report.copy(
                            verificationStatus = status,
                            lastUpdatedAt = System.currentTimeMillis()
                        )
                    } else {
                        report
                    }
                }
                onSuccess()
            },
            onError = onError
        )
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _error.value = null
    }
}
