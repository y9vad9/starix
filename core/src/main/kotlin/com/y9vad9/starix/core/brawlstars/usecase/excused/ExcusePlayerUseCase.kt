package com.y9vad9.starix.core.brawlstars.usecase.excused

import com.y9vad9.starix.foundation.time.TimeProvider
import com.y9vad9.starix.foundation.time.UnixTime
import com.y9vad9.starix.core.brawlstars.entity.club.value.ClubTag
import com.y9vad9.starix.core.brawlstars.entity.player.value.PlayerTag
import com.y9vad9.starix.core.brawlstars.repository.BrawlStarsRepository
import com.y9vad9.starix.core.system.entity.isAdminIn
import com.y9vad9.starix.core.system.repository.SettingsRepository
import com.y9vad9.starix.core.telegram.entity.value.TelegramUserId

class ExcusePlayerUseCase(
    private val settingsRepository: SettingsRepository,
    private val brawlStarsRepository: BrawlStarsRepository,
    private val timeProvider: TimeProvider,
) {
    suspend fun execute(
        id: TelegramUserId,
        playerTag: List<PlayerTag>,
        clubTag: ClubTag,
        untilTime: UnixTime,
    ): Result {
        if (!settingsRepository.getSettings().isAdminIn(clubTag, id))
            return Result.NoPermission

        brawlStarsRepository.excuse(playerTag, clubTag, untilTime, timeProvider.provide())
            .onFailure { return Result.Failure(it) }

        return Result.Success
    }

    sealed interface Result {
        data object Success : Result
        data object NoPermission : Result
        data class Failure(val error: Throwable) : Result
    }
}