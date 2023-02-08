package com.peyess.salesapp.di

import android.content.Context
import androidx.room.Room
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.room.database.ActiveSalesDatabase
import com.peyess.salesapp.data.room.database.CacheCreateClientDatabase
import com.peyess.salesapp.data.room.database.EditSaleDatabase
import com.peyess.salesapp.data.room.database.LocalClientDatabase
import com.peyess.salesapp.data.room.database.PictureUploadDatabase
import com.peyess.salesapp.data.room.database.ProductsDatabase
import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameterFactory
import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameterFactoryImpl
import com.peyess.salesapp.screen.sale.frames_measure.animation.mediator.MediatorFactory
import com.peyess.salesapp.screen.sale.frames_measure.animation.mediator.MediatorFactoryImpl
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.AnimationParametersFactory
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.AnimationParametersFactoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext context: Context): SalesApplication {
        return context as SalesApplication
    }

    @Provides
    @Singleton
    fun provideActiveSalesDatabase(@ApplicationContext context: Context): ActiveSalesDatabase {
        return Room.databaseBuilder(
                context,
                ActiveSalesDatabase::class.java,
                "active_sales.db",
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEditSaleDatabase(@ApplicationContext context: Context): EditSaleDatabase {
        return Room.databaseBuilder(
                context,
                EditSaleDatabase::class.java,
                "picture_upload.db",
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsDatabase(@ApplicationContext context: Context): ProductsDatabase {
        return Room.databaseBuilder(
                context,
                ProductsDatabase::class.java,
                "products.db",
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCacheCreateClientDatabase(
        @ApplicationContext context: Context,
    ): CacheCreateClientDatabase {
        return Room.databaseBuilder(
                context,
                CacheCreateClientDatabase::class.java,
                "cache_create_client.db",
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalClientDatabase(
        @ApplicationContext context: Context,
    ): LocalClientDatabase {
        return Room.databaseBuilder(
                context,
                LocalClientDatabase::class.java,
                "local_client.db",
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePictureUploadDatabase(
        @ApplicationContext context: Context,
    ): PictureUploadDatabase {
        return Room.databaseBuilder(
                context,
                PictureUploadDatabase::class.java,
                "picture_upload_management.db"
            // TODO: remove destructive migration
            ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMediatorFactory(): MediatorFactory {
        return MediatorFactoryImpl()
    }

    @Provides
    @Singleton
    fun provideMeasuringParameterFactory(): MeasuringParameterFactory {
        return MeasuringParameterFactoryImpl()
    }

    @Provides
    @Singleton
    fun provideAnimationParametersFactory(
        measuringParameterFactory: MeasuringParameterFactory,
        mediatorParameterFactory: MediatorFactory,
    ): AnimationParametersFactory {
        return AnimationParametersFactoryImpl(measuringParameterFactory, mediatorParameterFactory)
    }
}