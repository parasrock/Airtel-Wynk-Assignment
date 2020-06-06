package com.paras.baseapplication.persistance.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.paras.baseapplication.application.BaseApplication
import com.paras.baseapplication.persistance.daos.NewsDao
import com.paras.baseapplication.persistance.entities.NewsEntity

/**
 * Created by parasj on 1/9/18.
 */
@Database(
    entities = [NewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {

        private var INSTANCE: MyDatabase? = null

        fun getInstance(): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        BaseApplication.instance,
                        MyDatabase::class.java, "my_db.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }

}