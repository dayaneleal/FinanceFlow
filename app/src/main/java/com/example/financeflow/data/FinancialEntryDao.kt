package com.example.financeflow.data


    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.Query
    import androidx.room.Update
    import com.example.financeflow.domain.FinancialEntry
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface FinancialEntryDao {

        @Insert
        suspend fun insert(entry: FinancialEntry)

        @Update
        suspend fun update(entry: FinancialEntry)

        @Delete
        suspend fun delete(entry: FinancialEntry)


        @Query("SELECT * from financial_entries ORDER BY id DESC")
        fun getAll(): Flow<List<FinancialEntry>>
    }