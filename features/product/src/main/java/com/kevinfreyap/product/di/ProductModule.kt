package com.kevinfreyap.product.di

import com.kevinfreyap.domain.repository.IProductStatsRepository
import com.kevinfreyap.product.data.repository.CategoryRepository
import com.kevinfreyap.product.data.manager.SyncManager
import com.kevinfreyap.product.data.repository.LocationRepository
import com.kevinfreyap.product.data.repository.ProductRepository
import com.kevinfreyap.product.data.repository.StockBatchRepository
import com.kevinfreyap.product.data.repository.SyncRepository
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.repository.ILocationRepository
import com.kevinfreyap.product.domain.repository.IProductRepository
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import com.kevinfreyap.product.domain.repository.ISyncRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductModule {
    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepository
    ): ICategoryRepository

    @Binds
    abstract fun bindProductRepository(
        impl: ProductRepository
    ): IProductRepository

    @Binds
    abstract fun bindProductStatsRepository(
        impl: ProductRepository
    ): IProductStatsRepository

    @Binds
    abstract fun bindLocationRepository(
        impl: LocationRepository
    ): ILocationRepository

    @Binds
    abstract fun bindStockBatchRepository(
        impl: StockBatchRepository
    ): IStockBatchRepository

    @Binds
    abstract fun bindSyncRepository(
        impl: SyncRepository
    ): ISyncRepository

    @Binds
    abstract fun bindSyncManager(
        impl: SyncManager
    ): ISyncManager
}