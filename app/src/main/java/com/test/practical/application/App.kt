package com.test.practical.application

import android.app.Application

/**
 * Created by Ali Asadi on 10/03/2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object{
        private var sInstance: App? = null

        fun getInstance(): App? {
            return sInstance
        }
    }

}
