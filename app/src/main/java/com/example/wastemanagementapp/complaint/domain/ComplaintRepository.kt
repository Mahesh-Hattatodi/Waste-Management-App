package com.example.wastemanagementapp.complaint.domain


interface ComplaintRepository {

    suspend fun submitComplaint(complaintInfo: ComplaintInfo)
}