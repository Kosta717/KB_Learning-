package dev.kbwallet.app.portfolio.presentation

data class UiPortfolioCoinItem(
    val id: String,
    val name: String,
    val iconUrl: String,
    val amountInUnitText: String,
    val amountInFiatText: String,
    val performancePercentText: String,
    val isPositive: Boolean,
    val amountInFiat: Double = 0.0,
    val symbol: String = "",
)
