package com.kevinfreyap.user.presentation.action

import com.kevinfreyap.domain.model.AppRole

interface MembersAction {
    data object OnGenerateInviteCode: MembersAction
    data object OnDismissShareSheet : MembersAction
    data class OnApproveMember(val memberId: String, val role: AppRole): MembersAction
    data class OnRejectMember(val memberId: String): MembersAction
    data class OnShareInviteCodeClicked(val code: String): MembersAction
}