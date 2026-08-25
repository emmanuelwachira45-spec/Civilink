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
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
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
        newStatus: String
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
            },
            onError = { errorMessage ->
                _error.value = errorMessage
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
