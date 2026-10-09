package com.kevinfreyap.user.presentation.action

sealed interface EditProfileAction {
    data class OnImageUrlChanged(val url: String?): EditProfileAction

    sealed interface ProfilePictureAction: EditProfileAction {
        data object OnGalleryClicked: ProfilePictureAction
        data object OnCameraClicked: ProfilePictureAction
        data object OnRemoveImage: ProfilePictureAction
    }

    sealed interface UserDetailAction: EditProfileAction {
        data class OnNameChanged(val name: String): UserDetailAction
        data class OnPhoneNumberChanged(val phoneNumber: String): UserDetailAction
        data class OnJobTitleChanged(val jobTitle: String): UserDetailAction
    }

    data object UpdateProfile: EditProfileAction
}