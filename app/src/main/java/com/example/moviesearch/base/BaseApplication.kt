package com.example.moviesearch.base

import android.app.Application
import com.example.moviesearch.api.RequestApi
import com.example.moviesearch.api.RequestClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class BaseApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))

        bind<RequestClient>() with singleton { RequestClient() }
        bind<RequestApi>() with singleton { instance<RequestClient>().instance }
    }
}