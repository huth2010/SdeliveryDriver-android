package com.fpoly.sdeliverydriver.di.modules

import android.app.Application
import android.content.Context
import com.fpoly.sdeliverydriver.data.network.AuthApi
import com.fpoly.sdeliverydriver.data.network.ChatApi
import com.fpoly.sdeliverydriver.data.network.ContentDataSource
import com.fpoly.sdeliverydriver.data.network.DeliveryApi
import com.fpoly.sdeliverydriver.data.network.OrderApi
import com.fpoly.sdeliverydriver.data.network.PlacesApi
import com.fpoly.sdeliverydriver.data.network.RemoteDataSource
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.data.network.SocketManager
import com.fpoly.sdeliverydriver.data.network.UserApi
import com.fpoly.sdeliverydriver.data.repository.AuthRepository
import com.fpoly.sdeliverydriver.data.repository.ChatRepository
import com.fpoly.sdeliverydriver.data.repository.DeliveryRepository
import com.fpoly.sdeliverydriver.data.repository.HomeRepository
import com.fpoly.sdeliverydriver.data.repository.OrderRepository
import com.fpoly.sdeliverydriver.data.repository.PlacesRepository
import com.fpoly.sdeliverydriver.data.repository.UserRepository
import com.fpoly.sdeliverydriver.ui.call.call.WebRTCClient
import dagger.Module
import dagger.Provides

@Module
object NetworkModule {

    @Provides
    fun providerWebRTCClient(
        context: Context,
    ) : WebRTCClient = WebRTCClient(context as Application)
    @Provides
    fun providerSessionManager(
        context: Context
    ) : SessionManager = SessionManager(context.applicationContext)
    @Provides
    fun providerContentDataSource(
        context: Context
    ) : ContentDataSource = ContentDataSource(context.contentResolver)


    @Provides
    fun providerRemoteDateSource(): RemoteDataSource = RemoteDataSource()

    @Provides
    fun providerHomeRepository(
        api: OrderApi,
        contentDataSource: ContentDataSource
    ): HomeRepository = HomeRepository(api, contentDataSource)

    @Provides
    fun providerApiUser(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): UserApi = remoteDataSource.buildApi(UserApi::class.java, context)

    @Provides
    fun providerUserRepository(
        api: UserApi
    ): UserRepository = UserRepository(api)
    @Provides
    fun providerApiDelivery(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): DeliveryApi = remoteDataSource.buildApi(DeliveryApi::class.java, context)

    @Provides
    fun providerDeliveryRepository(
        api: DeliveryApi
    ): DeliveryRepository = DeliveryRepository(api)

    @Provides
    fun providerApiPlaces(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): PlacesApi = remoteDataSource.buildApiOpenStreetMap(PlacesApi::class.java, context)

    @Provides
    fun providerPlacesRepository(
        api: PlacesApi
    ): PlacesRepository = PlacesRepository(api)

    @Provides
    fun providerApiOrder(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): OrderApi = remoteDataSource.buildApi(OrderApi::class.java, context)

    @Provides
    fun providerOrderRepository(
        api: OrderApi
    ): OrderRepository = OrderRepository(api)

    @Provides
    fun providerApiAuth(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): AuthApi = remoteDataSource.buildApi(AuthApi::class.java, context)

    @Provides
    fun providerAuthRepository(
        api: AuthApi
    ): AuthRepository = AuthRepository(api)

    @Provides
    fun providerApiChat(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): ChatApi = remoteDataSource.buildApi(ChatApi::class.java, context)

    @Provides
    fun providerChatRepository(
        chatApi: ChatApi,
        authApi: AuthApi,
        socketManager: SocketManager,
        contentDataSource: ContentDataSource
    ): ChatRepository = ChatRepository(chatApi, authApi, socketManager, contentDataSource)

    @Provides
    fun providerSocketManger(
    ): SocketManager = SocketManager()
}

