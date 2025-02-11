package com.example.wastemanagementapp.complaint.data

import com.example.wastemanagementapp.complaint.domain.ComplaintInfo
import com.example.wastemanagementapp.complaint.domain.ComplaintRepository
import javax.inject.Inject

class ComplaintRepoImpl @Inject constructor() : ComplaintRepository {

    override suspend fun submitComplaint(complaintInfo: ComplaintInfo) {
        TODO("Not yet implemented")
    }
}
