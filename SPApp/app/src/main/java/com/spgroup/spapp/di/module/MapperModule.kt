package com.spgroup.spapp.di.module

import com.spgroup.spapp.repository.mapper.*
import dagger.Module
import dagger.Provides

@Module
object MapperModule {

    @JvmStatic
    @Provides
    fun providePartnerMapper() = PartnerMapper()

    @JvmStatic
    @Provides
    fun providePromotionMapper() = PromotionMapper()

    @JvmStatic
    @Provides
    fun provideRequestAckMapper() = RequestAckMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelCatMapper() = TopLevelCatMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelPromoMapper() = TopLevelPromotionMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelPartnerMapper() = TopLevelFeaturedPartnerMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelVariableMapper() = TopLevelVariableMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelPageSectionMapper() = TopLevelPageSectionMapper()

    @JvmStatic
    @Provides
    fun provideTopLevelPageMapper(topLevelPageSectionMapper: TopLevelPageSectionMapper): TopLevelPageMapper {
        return TopLevelPageMapper(topLevelPageSectionMapper)
    }

    @JvmStatic
    @Provides
    fun provideHomeDataMapper(
            topLevelCatMapper: TopLevelCatMapper,
            topLevelPromotionMapper: TopLevelPromotionMapper,
            topLevelFeaturedPartnerMapper: TopLevelFeaturedPartnerMapper,
            topLevelPageMapper: TopLevelPageMapper,
            topLevelVariableMapper: TopLevelVariableMapper
    ): HomeDataMapper {
        return HomeDataMapper(
                topLevelCatMapper,
                topLevelPromotionMapper,
                topLevelFeaturedPartnerMapper,
                topLevelPageMapper,
                topLevelVariableMapper)
    }
}