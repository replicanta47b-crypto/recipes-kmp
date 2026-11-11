package com.example.restapi.di

import com.example.restapi.data.auth.AuthRepositoryImpl
import com.example.restapi.data.network.createHttpClient
import com.example.restapi.data.onboarding.SettingsOnboardingRepository
import com.example.restapi.data.security.SecureTokenStorage
import com.example.restapi.domain.auth.AuthRepository
import com.example.restapi.domain.auth.GetCurrentUserUseCase
import com.example.restapi.domain.auth.IsAuthorizedUseCase
import com.example.restapi.domain.auth.LoginUseCase
import com.example.restapi.domain.auth.LogoutUseCase
import com.example.restapi.domain.auth.TokenStorage
import com.example.restapi.domain.auth.ValidatePasswordUseCase
import com.example.restapi.domain.auth.ValidateUsernameUseCase
import com.example.restapi.domain.onboarding.CompleteOnboardingUseCase
import com.example.restapi.domain.onboarding.IsOnboardingCompletedUseCase
import com.example.restapi.domain.onboarding.OnboardingRepository
import com.example.restapi.domain.onboarding.ResetOnboardingUseCase
import com.example.restapi.ui.auth.AuthViewModel
import com.example.restapi.ui.home.HomeViewModel
import com.example.restapi.ui.main.MainViewModel
import com.example.restapi.ui.onboarding.OnboardingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    // data
    single<Settings> { Settings() }
    singleOf(::SettingsOnboardingRepository) { bind<OnboardingRepository>() }
    singleOf(::SecureTokenStorage) { bind<TokenStorage>() }
    single { createHttpClient(get()) }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    // domain
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::ResetOnboardingUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::IsAuthorizedUseCase)

    // presentation
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}
