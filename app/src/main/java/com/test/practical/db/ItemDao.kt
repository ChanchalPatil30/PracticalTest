package com.test.practical.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.practical.model.Items

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(responseData: List<Items>)

    @Query("SELECT * FROM Items")
    fun getAllItems(): LiveData<List<Items>>


    @Query("DELETE FROM Items")
    fun deleteAllData()
}