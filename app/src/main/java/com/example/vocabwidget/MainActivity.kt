package com.example.vocabwidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.ui.theme.*

enum class Screen {
    Welcome, Home, Detail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf(Screen.Welcome) }
            var selectedWord by remember { mutableStateOf<Word?>(null) }
            var isDarkMode by remember { mutableStateOf(false) }

            VocabWidgetTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ScreenTransition"
                    ) { screen ->
                        when (screen) {
                            Screen.Welcome -> WelcomeScreen { currentScreen = Screen.Home }
                            Screen.Home -> HomeScreen(
                                onWordClick = {
                                    selectedWord = it
                                    currentScreen = Screen.Detail
                                },
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = !isDarkMode }
                            )
                            Screen.Detail -> DetailScreen(
                                word = selectedWord ?: VocabRepository.getRandomWord(),
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueDark, BluePrimary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Vocab",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Find Your Dream\nKnowledge With Us",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(64.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onStart() }
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onWordClick: (Word) -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val allWords = remember { VocabRepository.getAllWords() }
    val categories = listOf("Most Viewed", "Nearby", "Latest")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = Color.Transparent)
                )
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.AccessTime, contentDescription = null) })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = null) })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = null) })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hi, David 👋", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = "Explore the world", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleDarkMode) {
                        Icon(imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Search
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search words...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                trailingIcon = { Icon(Icons.Default.Tune, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Popular places", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                TextButton(onClick = {}) { Text("View all", color = Color.Gray) }
            }

            // Categories
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.Black else Color.Transparent)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(text = category, color = if (isSelected) Color.White else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(allWords) { word ->
                    WordCard(word = word, onClick = { onWordClick(word) })
                }
            }
        }
    }
}

@Composable
fun WordCard(word: Word, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(240.dp).height(340.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(colors = listOf(BluePrimary, BlueDark))))
            
            Box(modifier = Modifier.padding(16.dp).size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).align(Alignment.TopEnd), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.BookmarkBorder, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Box(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(20.dp)).background(Color.Black.copy(alpha = 0.3f)).padding(12.dp)) {
                Column {
                    Text(text = word.term, style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(12.dp))
                            Text(text = word.origin, style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(12.dp))
                            Text(text = "4.8", style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(word: Word, onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(460.dp).background(Brush.verticalGradient(colors = listOf(BlueDark, BluePrimary)))) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 56.dp, start = 24.dp, end = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onBack, modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f))) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = {}, modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.2f))) {
                    Icon(Icons.Outlined.BookmarkBorder, contentDescription = null, tint = Color.White)
                }
            }

            Card(
                modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 24.dp).offset(y = 45.dp).fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = word.term, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Text(text = word.origin, style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Level", color = Color.Gray, fontSize = 12.sp)
                        Text(text = word.level, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = BluePrimary))
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(top = 530.dp, start = 24.dp, end = 24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Text(text = "Overview", fontWeight = if(selectedTab == 0) FontWeight.Bold else FontWeight.Normal, color = if(selectedTab == 0) BluePrimary else Color.Gray, modifier = Modifier.clickable { selectedTab = 0 })
                Text(text = "Details", fontWeight = if(selectedTab == 1) FontWeight.Bold else FontWeight.Normal, color = if(selectedTab == 1) BluePrimary else Color.Gray, modifier = Modifier.clickable { selectedTab = 1 })
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                DetailStat(icon = Icons.Default.AccessTime, value = "10 min", label = "Learning")
                DetailStat(icon = Icons.Default.Bolt, value = "High", label = "Usage")
                DetailStat(icon = Icons.Default.Star, value = "4.8", label = "Mastery")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (selectedTab == 0) {
                Column {
                    Text(text = word.definition, style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray, lineHeight = 26.sp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Example: \"${word.example}\"", style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
                }
            } else {
                Column {
                    Text(text = "Pronunciation: ${word.pronunciation}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Synonyms: ${word.synonyms.joinToString(", ")}", color = Color.Gray)
                    Text(text = "Antonyms: ${word.antonyms.joinToString(", ")}", color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(64.dp).padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Mark as Learned", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun DetailStat(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = BluePrimary)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() = VocabWidgetTheme { WelcomeScreen {} }

@Preview(showBackground = true)
@Composable
fun HomePreview() = VocabWidgetTheme { HomeScreen({}, false, {}) }

@Preview(showBackground = true)
@Composable
fun DetailPreview() = VocabWidgetTheme { DetailScreen(VocabRepository.getRandomWord(), {}) }
