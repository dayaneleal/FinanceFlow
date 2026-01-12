package com.example.financeflow.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financeflow.domain.FinancialEntry

     // Lista todas as tabelas (entities) do seu banco. Se criar outra tabela, adicione aqui.
     @Database(entities = [FinancialEntry::class], version = 1, exportSchema = false)
     abstract class AppDatabase : RoomDatabase() {

         // Permite acessar o DAO que você acabou de criar
         abstract fun financialEntryDao(): FinancialEntryDao

         companion object {
             @Volatile
             private var Instance: AppDatabase? = null

             fun getDatabase(context: Context): AppDatabase {
                 // Se já existe, retorna o banco aberto. Se não, cria um novo.
                 return Instance ?: synchronized(this) {
                     Room.databaseBuilder(
                         context.applicationContext,
                         AppDatabase::class.java,
                         "finance_flow_database" // Nome do arquivo que aparecerá no App Inspection
                     )
                         // .fallbackToDestructiveMigration() // Dica: Útil em desenvolvimento se você mudar tabelas
                         .build()
                         .also { Instance = it }
                 }
             }
         }
     }
