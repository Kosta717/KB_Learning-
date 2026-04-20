package dev.kbwallet.app.portfolio.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import dev.kbwallet.app.portfolio.presentation.component.DonutChart
import dev.kbwallet.app.theme.LocalKBWalletColorsPalette
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    onCoinItemClicked: (String) -> Unit,
    onDiscoverCoinsClicked: () -> Unit,
) {
    val portfolioViewModel = koinViewModel<PortfolioViewModel>()
    val state by portfolioViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portfolio", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else {
            PortfolioContent(
                state = state,
                modifier = Modifier.padding(paddingValues),
                onCoinItemClicked = onCoinItemClicked,
                onDiscoverCoinsClicked = onDiscoverCoinsClicked
            )
        }
    }
}

@Composable
fun PortfolioContent(
    state: PortfolioState,
    modifier: Modifier = Modifier,
    onCoinItemClicked: (String) -> Unit,
    onDiscoverCoinsClicked: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            PortfolioBalanceSection(
                portfolioValue = state.portfolioValue,
                totalChange = "+5.67% (24h)" // Mocked for UI
            )
        }

        item {
            if (state.coins.isNotEmpty()) {
                AssetDistributionCard(coins = state.coins)
            } else {
                PortfolioEmptySection(onDiscoverCoinsClicked = onDiscoverCoinsClicked)
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search assets...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                )
            )
        }

        item {
            Text(
                text = "Your Holdings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        val filteredCoins = state.coins.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.symbol.contains(searchQuery, ignoreCase = true)
        }

        items(filteredCoins) { coin ->
            CoinListItem(
                coin = coin,
                onCoinItemClicked = onCoinItemClicked
            )
        }
    }
}

@Composable
private fun PortfolioBalanceSection(
    portfolioValue: String,
    totalChange: String
) {
    Column {
        Text(
            text = "Total Portfolio Value",
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = portfolioValue,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "↗ $totalChange", // Simple mock
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun AssetDistributionCard(coins: List<UiPortfolioCoinItem>) {
    val totalFiat = coins.sumOf { it.amountInFiat }
    val sortedCoins = coins.sortedByDescending { it.amountInFiat }
    val chartValues = sortedCoins.map { it.amountInFiat.toFloat() }
    
    // Generate different shades of green based on the primary color for the donut chart
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        Color(0xFF00FF00).copy(alpha = 0.5f),
        Color(0xFF00FF00).copy(alpha = 0.3f),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Asset Distribution",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                DonutChart(
                    values = chartValues,
                    colors = colors
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Legend
            val rows = sortedCoins.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        row.forEachIndexed { index, coin ->
                            val color = colors.getOrElse(sortedCoins.indexOf(coin)) { Color.Gray }
                            val percentage = if (totalFiat > 0) (coin.amountInFiat / totalFiat * 100) else 0.0
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = coin.symbol.uppercase(),
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "${((percentage * 10).roundToInt() / 10.0)}%",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f)) // Padding for odd number of items
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoinListItem(
    coin: UiPortfolioCoinItem,
    onCoinItemClicked: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onCoinItemClicked.invoke(coin.id) }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = coin.iconUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF2A2A2A)).padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coin.symbol.uppercase(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = coin.amountInFiatText,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                val isPositive = coin.isPositive
                Text(
                    text = (if (isPositive) "↗ " else "↘ ") + coin.performancePercentText,
                    color = if (isPositive) MaterialTheme.colorScheme.primary else LocalKBWalletColorsPalette.current.lossRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PortfolioEmptySection(
    onDiscoverCoinsClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = "Your portfolio is empty.",
            color = Color.Gray,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onDiscoverCoinsClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Discover Coins")
        }
    }
}