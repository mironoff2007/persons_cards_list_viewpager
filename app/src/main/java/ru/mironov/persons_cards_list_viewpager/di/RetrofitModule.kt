package ru.mironov.persons_cards_list_viewpager.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://stoplight.io/mocks/kode-education/trainee-test/25143926/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls()
                        .create()
                )
            )
    }

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit.Builder): UsersApi {
        return retrofit
            .build()
            .create(UsersApi::class.java)
    }
}