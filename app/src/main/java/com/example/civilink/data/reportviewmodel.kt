package com.example.civilink.data

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel(
    private val repository: ReportRepository = ReportRepository()
) : ViewModel() {

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadReports()
    }

    fun loadReports() {
        _isLoading.value = true
        _error.value = null
        
        repository.getReports(
            onSuccess = { reportsList ->
                _reports.value = reportsList
                _isLoading.value = false
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
    }

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

    fun createReport(
        report: Report,
        onSuccess: (Report) -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        repository.createReport(
            report = report,
            onSuccess = { newReport ->
                _isLoading.value = false
                onSuccess(newReport)
            },
            onError = { errorMessage ->
                _isLoading.value = false
                onError(errorMessage)
            }
        )
    }

    fun updateReportStatus(
        reportId: String,
        newStatus: String
    ) {
        repository.updateReportStatus(
            reportId = reportId,
            newStatus = newStatus,
            onSuccess = {
                // Status updated successfully
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }
    
    fun clearError() {
        _error.value = null
    }
}
