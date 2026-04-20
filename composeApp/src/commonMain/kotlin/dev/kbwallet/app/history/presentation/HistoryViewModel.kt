package dev.kbwallet.app.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kbwallet.app.portfolio.data.local.TransactionEntity
import dev.kbwallet.app.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HistoryState(
    val isLoading: Boolean = true,
    val transactions: List<TransactionEntity> = emptyList(),
    val totalTrades: Int = 0,
    val totalBuy: Int = 0,
    val totalSell: Int = 0
)

class HistoryViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {

    val state: StateFlow<HistoryState> = portfolioRepository.getTransactions()
        .map { txs ->
            HistoryState(
                isLoading = false,
                transactions = txs,
                totalTrades = txs.size,
                totalBuy = txs.count { it.type == "BUY" },
                totalSell = txs.count { it.type == "SELL" }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryState(isLoading = true)
        )
}
