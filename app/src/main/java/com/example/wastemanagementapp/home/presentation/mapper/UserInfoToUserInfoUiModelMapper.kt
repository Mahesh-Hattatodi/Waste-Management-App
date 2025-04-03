package com.example.wastemanagementapp.home.presentation.mapper

import com.example.wastemanagementapp.core.util.Mapper
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import javax.inject.Inject

class UserInfoToUserInfoUiModelMapper @Inject constructor(): Mapper<UserInfo, UserinfoUiModel> {
    override fun map(from: UserInfo): UserinfoUiModel {
        return UserinfoUiModel(
            displayName = from.displayName,
            email = from.email,
            photoUrl = from.photoUrl,
            ranking = from.ranking
        )
    }
}