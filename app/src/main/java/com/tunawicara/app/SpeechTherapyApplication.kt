package com.tunawicara.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Tuna Wicara
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class SpeechTherapyApplication : Application()
