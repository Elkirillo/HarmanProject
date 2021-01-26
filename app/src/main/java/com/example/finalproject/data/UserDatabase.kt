package com.example.finalproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finalproject.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile  //значение будет видно остальным потокам
        private  var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context) : UserDatabase {
            val tempItem = INSTANCE
            if (tempItem != null){
                return tempItem
            }
            //если нашего INSTANCE не существуем
            //используем блок synchronize, для того, чтобы обезопасить ресурсы
            synchronized( this){
                val instance =  Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}