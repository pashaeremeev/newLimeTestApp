package com.example.new_practice.app.di

import com.example.new_practice.domain.repos.ChannelRepository
import com.example.new_practice.domain.repos.EpgRepository
import com.example.new_practice.domain.usecase.ChangeFavoriteChannelUseCase
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgByChannelIdUseCase
import com.example.new_practice.domain.usecase.GetEpgsUseCase
import com.example.new_practice.domain.usecase.SearchChannelsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideChangeFavoriteChannelUseCase(channelRepository: ChannelRepository): ChangeFavoriteChannelUseCase {
        return ChangeFavoriteChannelUseCase(channelRepository = channelRepository)
    }

    @Provides
    fun provideGetChannelsInfoUseCase(channelRepository: ChannelRepository): GetChannelsInfoUseCase {
        return GetChannelsInfoUseCase(channelRepository = channelRepository)
    }

    @Provides
    fun provideGetEpgByChannelIdUseCase(epgRepository: EpgRepository): GetEpgByChannelIdUseCase {
        return GetEpgByChannelIdUseCase(epgRepository = epgRepository)
    }

    @Provides
    fun provideGetEpgsUseCase(epgRepository: EpgRepository): GetEpgsUseCase {
        return GetEpgsUseCase(epgRepository = epgRepository)
    }

    @Provides
    fun provideSearchChannelsUseCase(channelRepository: ChannelRepository): SearchChannelsUseCase {
        return SearchChannelsUseCase(channelRepository = channelRepository)
    }

}