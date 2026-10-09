package com.kevinfreyap.product.data.network.resources

import com.kevinfreyap.network.resource.GroupResource
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/sync")
class SyncResource (val parent: GroupResource.Id) {
    @Serializable
    @Resource("pull")
    data class Pull(val parent: SyncResource, val updatedAfter: Long = 0L)
}