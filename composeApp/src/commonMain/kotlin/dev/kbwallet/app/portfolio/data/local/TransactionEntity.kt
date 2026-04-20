package dev.kbwallet.app.portfolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val amountFiat: Double,
    val amountCrypto: Double,
    val price: Double,
    val timestamp: Long
)
