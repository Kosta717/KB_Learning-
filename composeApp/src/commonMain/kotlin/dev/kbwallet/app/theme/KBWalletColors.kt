package dev.kbwallet.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class KBWalletColorsPalette(
    var profitGreen: Color = Color.Unspecified,
    var lossRed: Color = Color.Unspecified,
)

val ProfitGreenColor = Color(color = 0xFF00FF00)
val LossRedColor = Color(color = 0xFFFF3B30)

val DarkProfitGreenColor = Color(color = 0xFF00FF00)
val DarkLossRedColor = Color(color = 0xFFFF3B30)

val LigthKBWalletColorsPalette = KBWalletColorsPalette(
    profitGreen = ProfitGreenColor,
    lossRed = LossRedColor
)

val DarkKBWalletColorsPalette = KBWalletColorsPalette(
    profitGreen = DarkProfitGreenColor,
    lossRed = DarkLossRedColor
)

val LocalKBWalletColorsPalette = compositionLocalOf { KBWalletColorsPalette() }