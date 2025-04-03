package com.example.wastemanagementapp.feedback.domain.use_case

import javax.inject.Inject

class GetRatingFromEmojiUseCase @Inject constructor() {

    private val emojiToInt = mapOf(
        "😡" to 1,  // Angry
        "🙁" to 2,  // Slightly Sad
        "😐" to 3,  // Neutral
        "🙂" to 4,  // Slightly Happy
        "😁" to 5   // Very Happy
    )

    operator fun invoke(emoji: String) : Int {
        return emojiToInt[emoji] ?: -1
    }
}