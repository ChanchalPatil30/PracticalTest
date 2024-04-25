package com.test.practical.db

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Query
import androidx.room.Room
import com.test.practical.application.App
import com.test.practical.model.Items

class ItemsRepository() {

    private var sInstance: ItemsDatabase? = null

    init {
        if (sInstance == null) {
            sInstance =
                Room.databaseBuilder(
                    App.getInstance()!!,
                    ItemsDatabase::class.java,
                    "database"
                ).build()
        }
    }

    fun insertData(respModel: List<Items>) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                sInstance?.itemsDao()
                    ?.insertItems(respModel)
                return null
            }
        }.execute()
    }

    fun getData(): LiveData<List<Items>>? {
        return sInstance?.itemsDao()?.getAllItems()
    }

    fun deleteAll() {
        object : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            sInstance?.itemsDao()?.deleteAllData()
            return null
        }
        }.execute()
    }


}