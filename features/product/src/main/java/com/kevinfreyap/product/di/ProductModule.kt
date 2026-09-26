package com.kevinfreyap.product.di

import com.kevinfreyap.product.data.repository.CategoryRepository
import com.kevinfreyap.product.data.manager.ImageManager
import com.kevinfreyap.product.data.manager.SyncManager
import com.kevinfreyap.product.data.repository.LocationRepository
import com.kevinfreyap.product.data.repository.ProductRepository
import com.kevinfreyap.product.data.repository.StockBatchRepository
import com.kevinfreyap.product.data.repository.SyncRepository
import com.kevinfreyap.product.data.repository.TransactionRepository
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import com.kevinfreyap.product.domain.manager.IImageManager
import com.kevinfreyap.product.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.repository.ILocationRepository
import com.kevinfreyap.product.domain.repository.IProductRepository
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import com.kevinfreyap.product.domain.repository.ISyncRepository
import com.kevinfreyap.product.domain.repository.ITransactionRepository
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
    abstract fun bindTransactionRepository(
        impl: TransactionRepository
    ): ITransactionRepository

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
    abstract fun bindImageManager(
        impl: ImageManager
    ): IImageManager

    @Binds
    abstract fun bindSyncManager(
        impl: SyncManager
    ): ISyncManager
}