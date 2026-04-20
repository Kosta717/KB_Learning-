package dev.kbwallet.app.trade.domain

import dev.kbwallet.app.core.domain.DataError
import dev.kbwallet.app.core.domain.EmptyResult
import dev.kbwallet.app.core.domain.Result
import dev.kbwallet.app.core.domain.coin.Coin
import dev.kbwallet.app.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import dev.kbwallet.app.portfolio.data.local.TransactionEntity

class SellCoinUseCase(
    private val portfolioRepository: PortfolioRepository,
    ) {
    suspend fun sellCoin(
        coin: Coin,
        amountInFiat: Double,
        price: Double,
        ): EmptyResult<DataError> {
        val sellAllThreshold = 1
        when(val existingCoinResponse = portfolioRepository.getPortfolioCoin(coin.id)) {
            is Result.Success -> {
                val existingCoin = existingCoinResponse.data
                val sellAmountInUnit = amountInFiat / price
                val balance = portfolioRepository.cashBalanceFlow().first()
                if (existingCoin == null || existingCoin.ownedAmountInUnit < sellAmountInUnit) {
                    return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
                }
                val remainingAmountFiat = existingCoin.ownedAmountInFiat - amountInFiat
                val remainingAmountUnit = existingCoin.ownedAmountInUnit - sellAmountInUnit
                if (remainingAmountFiat < sellAllThreshold) {
                    portfolioRepository.removeCoinFromPortfolio(coin.id)
                } else {
                    portfolioRepository.savePortfolioCoin(
                        existingCoin.copy(
                            ownedAmountInUnit = remainingAmountUnit,
                            ownedAmountInFiat = remainingAmountFiat,
                            )
                    )
                }
                portfolioRepository.updateCashBalance(balance + amountInFiat)
                
                portfolioRepository.saveTransaction(
                    TransactionEntity(
                        type = "SELL",
                        coinId = coin.id,
                        coinName = coin.name,
                        coinSymbol = coin.symbol,
                        amountFiat = amountInFiat,
                        amountCrypto = sellAmountInUnit,
                        price = price,
                        timestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
                
                return Result.Success(Unit)
            }
            is Result.Error -> {
                return existingCoinResponse
            }
        }
    }
}