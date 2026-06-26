package com.bksd.core.presentation.util

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.AuthErrorType
import com.bksd.core.domain.error.LocationErrorType
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.error_auth_account_exists
import com.bksd.core.presentation.error_auth_invalid_credentials
import com.bksd.core.presentation.error_auth_social_cancelled
import com.bksd.core.presentation.error_auth_social_failed
import com.bksd.core.presentation.error_auth_user_not_found
import com.bksd.core.presentation.error_auth_weak_password
import com.bksd.core.presentation.error_location_permission_denied
import com.bksd.core.presentation.error_location_services_disabled
import com.bksd.core.presentation.error_location_unavailable
import com.bksd.core.presentation.error_media_file_too_large
import com.bksd.core.presentation.error_media_permission_denied
import com.bksd.core.presentation.error_media_recording_failed
import com.bksd.core.presentation.error_media_unsupported_format
import com.bksd.core.presentation.error_media_upload_failed
import com.bksd.core.presentation.error_network_conflict
import com.bksd.core.presentation.error_network_no_internet
import com.bksd.core.presentation.error_network_serialization
import com.bksd.core.presentation.error_network_server
import com.bksd.core.presentation.error_network_timeout
import com.bksd.core.presentation.error_network_too_many_requests
import com.bksd.core.presentation.error_network_unauthorized
import com.bksd.core.presentation.error_unknown
import org.jetbrains.compose.resources.StringResource

fun AppError.toUiText(): UiText = UiText.Resource(toStringResource())

private fun AppError.toStringResource(): StringResource = when (this) {
    is AppError.Network -> when (type) {
        NetworkErrorType.REQUEST_TIMEOUT -> Res.string.error_network_timeout
        NetworkErrorType.UNAUTHORIZED -> Res.string.error_network_unauthorized
        NetworkErrorType.CONFLICT -> Res.string.error_network_conflict
        NetworkErrorType.TOO_MANY_REQUESTS -> Res.string.error_network_too_many_requests
        NetworkErrorType.NO_INTERNET -> Res.string.error_network_no_internet
        NetworkErrorType.SERVER_ERROR -> Res.string.error_network_server
        NetworkErrorType.SERIALIZATION -> Res.string.error_network_serialization
    }

    is AppError.Auth -> when (type) {
        AuthErrorType.INVALID_CREDENTIALS -> Res.string.error_auth_invalid_credentials
        AuthErrorType.USER_NOT_FOUND -> Res.string.error_auth_user_not_found
        AuthErrorType.ACCOUNT_EXISTS -> Res.string.error_auth_account_exists
        AuthErrorType.WEAK_PASSWORD -> Res.string.error_auth_weak_password
        AuthErrorType.SOCIAL_LOGIN_CANCELLED -> Res.string.error_auth_social_cancelled
        AuthErrorType.SOCIAL_LOGIN_FAILED -> Res.string.error_auth_social_failed
    }

    is AppError.Media -> when (type) {
        MediaErrorType.PERMISSION_DENIED -> Res.string.error_media_permission_denied
        MediaErrorType.FILE_TOO_LARGE -> Res.string.error_media_file_too_large
        MediaErrorType.UPLOAD_FAILED -> Res.string.error_media_upload_failed
        MediaErrorType.RECORDING_FAILED -> Res.string.error_media_recording_failed
        MediaErrorType.UNSUPPORTED_FORMAT -> Res.string.error_media_unsupported_format
    }

    is AppError.Location -> when (type) {
        LocationErrorType.SERVICES_DISABLED -> Res.string.error_location_services_disabled
        LocationErrorType.PERMISSION_DENIED -> Res.string.error_location_permission_denied
        LocationErrorType.UNAVAILABLE -> Res.string.error_location_unavailable
    }

    is AppError.Unknown -> Res.string.error_unknown
}
