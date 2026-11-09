package com.example.restapi.di

import com.example.restapi.data.onboarding.SettingsOnboardingRepository
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

    // domain
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::ResetOnboardingUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)

    // presentation
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}
