package com.test.practical.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.practical.model.Items

@Database(entities = [Items::class], version = 1, exportSchema = false)
public abstract class ItemsDatabase : RoomDatabase() {

    abstract fun itemsDao(): ItemDao

}