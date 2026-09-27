package com.kevinfreyap.product.data.network.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/product")
class ProductResource {

    @Serializable
    @Resource("{id}")
    class Id(val parent: ProductResource = ProductResource(), val id: String) {

        @Serializable
        @Resource("image")
        class ProductImage(val parent: Id)
    }
}