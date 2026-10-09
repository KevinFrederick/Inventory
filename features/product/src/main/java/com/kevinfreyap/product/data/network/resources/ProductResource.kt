package com.kevinfreyap.product.data.network.resources

import com.kevinfreyap.network.resource.GroupResource
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/product")
class ProductResource (val parent: GroupResource.Id) {

    @Serializable
    @Resource("{id}")
    class Id(val parent: ProductResource, val id: String) {

        @Serializable
        @Resource("image")
        class ProductImage(val parent: Id)
    }
}