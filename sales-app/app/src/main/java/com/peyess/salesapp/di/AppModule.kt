package com.peyess.salesapp.di

import android.content.Context
import androidx.room.Room
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.data.database.CacheCreateClientDatabase
import com.peyess.salesapp.database.room.ProductsDatabase
import com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter.MeasuringParameterFactory
import com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter.MeasuringParameterFactoryImpl
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.MediatorFactory
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.MediatorFactoryImpl
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.AnimationParametersFactory
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.AnimationParametersFactoryImpl
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
            )
            .fallbackToDestructiveMigration()
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
            )
            .fallbackToDestructiveMigration()
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