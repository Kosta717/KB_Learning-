package dev.kbwallet.app.core.database.portfolio

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kbwallet.app.portfolio.data.local.PortfolioCoinEntity
import dev.kbwallet.app.portfolio.data.local.PortfolioDao
import dev.kbwallet.app.portfolio.data.local.UserBalanceDao
import dev.kbwallet.app.portfolio.data.local.UserBalanceEntity

import dev.kbwallet.app.portfolio.data.local.TransactionEntity
import dev.kbwallet.app.portfolio.data.local.TransactionDao

@ConstructedBy(PortfolioDatabaseCreator::class)
@Database(entities = [PortfolioCoinEntity::class, UserBalanceEntity::class, TransactionEntity::class], version = 3)
abstract class PortfolioDatabase: RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userBalanceDao(): UserBalanceDao
    abstract fun transactionDao(): TransactionDao
}