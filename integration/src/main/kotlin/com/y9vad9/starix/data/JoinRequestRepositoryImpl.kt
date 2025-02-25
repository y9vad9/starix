package com.y9vad9.starix.data

import com.y9vad9.starix.core.brawlstars.entity.club.value.ClubTag
import com.y9vad9.starix.core.common.entity.value.Count
import com.y9vad9.starix.core.common.entity.value.CustomMessage
import com.y9vad9.starix.core.telegram.entity.value.TelegramUserId
import com.y9vad9.starix.core.system.entity.JoinRequest
import com.y9vad9.starix.core.system.repository.JoinRequestRepository
import com.y9vad9.starix.data.database.JoinRequestsTable
import com.y9vad9.starix.foundation.validation.annotations.ValidationDelicateApi
import com.y9vad9.starix.foundation.validation.createUnsafe
import kotlin.uuid.Uuid

class JoinRequestRepositoryImpl(
    private val joinRequestsTable: JoinRequestsTable,
) : JoinRequestRepository {
    @OptIn(ValidationDelicateApi::class)
    override suspend fun undecidedCount(): Count {
        return joinRequestsTable.count(JoinRequestsTable.Status.UNDECIDED)
            .let { Count.createUnsafe(it.toInt()) }
    }

    override suspend fun hasAnyFrom(userId: TelegramUserId): Boolean {
        return joinRequestsTable.hasAnyFromTgId(userId.value)
    }

    override suspend fun create(request: JoinRequest) {
        joinRequestsTable.create(
            id = request.id,
            playerTag = request.playerTag.toString(),
            clubTag = request.clubTag.toString(),
            message = request.message.toString(),
            tgId = request.tgId.value,
        )
    }

    override suspend fun removeFrom(userId: TelegramUserId) {
        joinRequestsTable.deleteByTelegramId(userId.value)
    }

    override suspend fun getUndecided(
        club: ClubTag?,
        maxSize: Count,
    ): List<JoinRequest> {
        return joinRequestsTable.getList(JoinRequestsTable.Status.UNDECIDED, maxSize.value)
    }

    override suspend fun getRequest(id: Uuid): JoinRequest? {
        return joinRequestsTable.get(id)
    }

    override suspend fun accept(uuid: Uuid, message: CustomMessage) {
        joinRequestsTable.updateStatus(uuid, JoinRequestsTable.Status.ACCEPTED)
    }

    override suspend fun decline(uuid: Uuid, message: CustomMessage) {
        joinRequestsTable.updateStatus(uuid, JoinRequestsTable.Status.DECLINED)
    }

}