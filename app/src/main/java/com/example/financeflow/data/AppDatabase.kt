package com.example.financeflow.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financeflow.domain.FinancialEntry


     @Database(entities = [FinancialEntry::class], version = 1, exportSchema = false)
     abstract class AppDatabase : RoomDatabase() {


         abstract fun financialEntryDao(): FinancialEntryDao

         companion object {
             @Volatile
             private var Instance: AppDatabase? = null

             fun getDatabase(context: Context): AppDatabase {

                 return Instance ?: synchronized(this) {
                     Room.databaseBuilder(
                         context.applicationContext,
                         AppDatabase::class.java,
                         "finance_flow_database"
                     )

                         .build()
                         .also { Instance = it }
                 }
             }
         }
     }
