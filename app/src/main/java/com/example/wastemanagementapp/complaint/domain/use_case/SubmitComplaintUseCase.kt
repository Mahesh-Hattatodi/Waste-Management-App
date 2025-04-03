package com.example.wastemanagementapp.complaint.domain.use_case

import com.example.wastemanagementapp.complaint.domain.ComplaintDataError
import com.example.wastemanagementapp.complaint.domain.models.ComplaintInfo
import com.example.wastemanagementapp.complaint.domain.repository.ComplaintRepository
import com.example.wastemanagementapp.core.util.Result
import javax.inject.Inject

class SubmitComplaintUseCase @Inject constructor(
    private val repository: ComplaintRepository
) {

    suspend operator fun invoke(complaintInfo: ComplaintInfo) : Result<Unit, ComplaintDataError> {
        return repository.saveComplaint(complaintInfo)
    }
}
