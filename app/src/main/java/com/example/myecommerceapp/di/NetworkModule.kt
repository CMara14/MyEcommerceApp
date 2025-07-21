package com.example.myecommerceapp.di

import com.example.myecommerceapp.BuildConfig
import com.example.myecommerceapp.data.remote.ApiService
import com.example.myecommerceapp.data.remote.AuthRemoteDataSource
import com.example.myecommerceapp.data.remote.AuthRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.myecommerceapp.data.remote.ProductsRemoteDataSource
import com.example.myecommerceapp.data.remote.ProductsRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductsRemoteDataSource(apiService: ApiService): ProductsRemoteDataSource {
        return ProductsRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(apiService: ApiService): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(apiService)
    }
}