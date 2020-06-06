package com.paras.baseapplication.persistance.daos

import androidx.room.*
import com.paras.baseapplication.persistance.entities.NewsEntity
import io.reactivex.Single

@Dao
interface NewsDao {

    @Query("SELECT * from response")
    fun getAllResponse(): Single<List<NewsEntity>>

    @Query("SELECT * from response where id = :id")
    fun getResponse(id: Int): Single<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(newsEntity: NewsEntity)

    @Delete
    fun deleteNews(newsEntity: NewsEntity)

}