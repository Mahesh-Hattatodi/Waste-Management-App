package com.example.wastemanagementapp.core.domain.use_case

import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.domain.repository.UpdateUserRepository
import com.example.wastemanagementapp.core.util.Result
import javax.inject.Inject

class UpdateUserPointsUseCase @Inject constructor(
    private val repository: UpdateUserRepository
) {
    suspend operator fun invoke(
        updateField: String,
        incrementBy: Int
    ) : Result<Unit, FirebaseFireStoreError> {
        return repository.updateUserPoints(
            updateField = updateField,
            incrementBy = incrementBy
        )
    }
}
