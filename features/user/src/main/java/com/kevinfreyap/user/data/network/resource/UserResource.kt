package com.kevinfreyap.user.data.network.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/me")
class UserResource {
    @Serializable
    @Resource("image")
    class Image(val parent: UserResource)
}