package dev.kbwallet.app.portfolio.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TransactionDao_Impl(
  __db: RoomDatabase,
) : TransactionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `transactions` (`id`,`type`,`coinId`,`coinName`,`coinSymbol`,`amountFiat`,`amountCrypto`,`price`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.type)
        statement.bindText(3, entity.coinId)
        statement.bindText(4, entity.coinName)
        statement.bindText(5, entity.coinSymbol)
        statement.bindDouble(6, entity.amountFiat)
        statement.bindDouble(7, entity.amountCrypto)
        statement.bindDouble(8, entity.price)
        statement.bindLong(9, entity.timestamp)
      }
    }
  }

  public override suspend fun insertTransaction(transaction: TransactionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity.insert(_connection, transaction)
  }

  public override fun getAllTransactions(): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _cursorIndexOfCoinId: Int = getColumnIndexOrThrow(_stmt, "coinId")
        val _cursorIndexOfCoinName: Int = getColumnIndexOrThrow(_stmt, "coinName")
        val _cursorIndexOfCoinSymbol: Int = getColumnIndexOrThrow(_stmt, "coinSymbol")
        val _cursorIndexOfAmountFiat: Int = getColumnIndexOrThrow(_stmt, "amountFiat")
        val _cursorIndexOfAmountCrypto: Int = getColumnIndexOrThrow(_stmt, "amountCrypto")
        val _cursorIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _cursorIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_cursorIndexOfType)
          val _tmpCoinId: String
          _tmpCoinId = _stmt.getText(_cursorIndexOfCoinId)
          val _tmpCoinName: String
          _tmpCoinName = _stmt.getText(_cursorIndexOfCoinName)
          val _tmpCoinSymbol: String
          _tmpCoinSymbol = _stmt.getText(_cursorIndexOfCoinSymbol)
          val _tmpAmountFiat: Double
          _tmpAmountFiat = _stmt.getDouble(_cursorIndexOfAmountFiat)
          val _tmpAmountCrypto: Double
          _tmpAmountCrypto = _stmt.getDouble(_cursorIndexOfAmountCrypto)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_cursorIndexOfPrice)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_cursorIndexOfTimestamp)
          _item =
              TransactionEntity(_tmpId,_tmpType,_tmpCoinId,_tmpCoinName,_tmpCoinSymbol,_tmpAmountFiat,_tmpAmountCrypto,_tmpPrice,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
