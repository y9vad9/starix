package com.y9vad9.bcm.core.system.usecase.join_request

import com.y9vad9.starix.core.common.entity.value.CustomMessage
import com.y9vad9.starix.core.system.repository.SettingsRepository
import com.y9vad9.starix.core.telegram.entity.value.TelegramUserId
import com.y9vad9.starix.core.system.repository.JoinRequestRepository
import com.y9vad9.starix.core.system.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeclineJoinRequestsUseCase(
    private val userRepository: UserRepository,
    private val joinRequests: JoinRequestRepository,
    private val settingsRepository: SettingsRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun execute(
        id: TelegramUserId,
        requestId: Uuid,
        message: CustomMessage,
    ): Result {
        userRepository.getById(id).getOrElse { return Result.Failure(it) }
        val request = joinRequests.getRequest(requestId) ?: return Result.NotFound
        val settings = settingsRepository.getSettings()

        val hasPermission = settings.allowedClubs[request.clubTag]?.admins?.let {
            id in it
        } == true || id in settings.admins

        if (!hasPermission) return Result.AccessDenied

        joinRequests.decline(requestId, message)
        return Result.Success
    }

    sealed interface Result {
        data object AccessDenied : Result
        data object Success : Result
        data object NotFound : Result
        data class Failure(val throwable: Throwable) : Result
    }
}