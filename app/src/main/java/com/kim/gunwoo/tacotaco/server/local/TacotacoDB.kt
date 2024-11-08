package com.kim.gunwoo.tacotaco.server.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TokenEntity::class], version = 5)
abstract class TacotacoDB : RoomDatabase(){
    abstract fun tokenDao(): TokenDAO

    companion object {
        private var instance: TacotacoDB? = null

        @Synchronized
        fun getInstance(context: Context): TacotacoDB? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TacotacoDB::class.java, "database"
                )
//                    .addMigrations(MIGRATION_4_5)
                    .build()
            }




            return instance
        }

        fun getInstanceOrNull(): TacotacoDB? {
            return instance
        }
//
//        private val MIGRATION_4_5 = object : Migration(4, 5) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE 'token_table' ADD COLUMN 'email' TEXT NOT NULL DEFAULT ''"
//                )
//            }
//
//        }
    }

}