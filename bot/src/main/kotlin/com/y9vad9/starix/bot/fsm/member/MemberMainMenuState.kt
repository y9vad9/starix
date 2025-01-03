package com.y9vad9.starix.bot.fsm.member

import com.y9vad9.starix.bot.fsm.FSMState
import com.y9vad9.starix.bot.fsm.common.CommonInitialState
import com.y9vad9.starix.bot.fsm.common.CommonSettingsMenuState
import com.y9vad9.starix.bot.fsm.getCurrentStrings
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContextWithFSM
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("GuestMainMenuState")
@Serializable
data class MemberMainMenuState(override val context: IdChatIdentifier) :
    MemberFSMState<MemberMainMenuState.Dependencies> {
    override suspend fun BehaviourContext.before(
        previousState: FSMState<*>,
        dependencies: Dependencies,
    ): FSMState<*> = with(dependencies) {
        val strings = getCurrentStrings(context)

        bot.send(
            chatId = context,
            entities = strings.member.enableNotificationsMessage,
            replyMarkup = ReplyKeyboardRemove(),
        )
        bot.send(
            chatId = context,
            text = strings.member.youAreInMemberMenuMessage,
            replyMarkup = replyKeyboard {
                row {
                    simpleButton(strings.guest.viewContactPersonsChoice)
                    simpleButton(strings.generalSettingsOption)
                }
            },
        )

        this@MemberMainMenuState
    }

    override suspend fun BehaviourContextWithFSM<in FSMState<*>>.process(
        dependencies: Dependencies,
    ): FSMState<*> = with(dependencies) {
        val strings = getCurrentStrings(context)

        return@with when (waitText().first().text) {
            "/start" -> CommonInitialState(context)
            strings.guest.viewContactPersonsChoice -> TODO()
            strings.generalSettingsOption -> CommonSettingsMenuState(
                context = context,
                callback = SettingsToMemberMenuCallback,
            )
            else -> {
                bot.send(
                    chatId = context,
                    text = strings.invalidChoiceMessage,
                )
                return this@MemberMainMenuState
            }
        }
    }

    @Serializable
    data object SettingsToMemberMenuCallback : CommonSettingsMenuState.Callback {
        override fun navigateBack(context: IdChatIdentifier): FSMState<*> {
            return MemberMainMenuState(context)
        }
    }

    interface Dependencies : FSMState.Dependencies
}