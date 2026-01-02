package com.rafaelmukhametov.habittrackerandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafaelmukhametov.habittrackerandroid.data.repository.HabitRepository
import com.rafaelmukhametov.habittrackerandroid.domain.model.Habit
import com.rafaelmukhametov.habittrackerandroid.ui.screen.*
import com.rafaelmukhametov.habittrackerandroid.ui.theme.HabitTrackerAndroidTheme
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.CreateHabitViewModel
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.HabitListViewModel
import com.rafaelmukhametov.habittrackerandroid.ui.viewmodel.HabitListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before setContent
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val application = application as HabitTrackerApplication
        val database = application.database
        val repository = HabitRepository(
            habitDao = database.habitDao(),
            completionDao = database.habitCompletionDao(),
            reminderDao = database.habitReminderDao()
        )
        
        setContent {
            HabitTrackerAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitTrackerApp(repository = repository)
                }
            }
        }
    }
}

@Composable
fun HabitTrackerApp(repository: HabitRepository) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.HabitList) }
    var showCreateHabit by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<Habit?>(null) }
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }
    
    val context = LocalContext.current
    val viewModel: HabitListViewModel = viewModel(
        factory = HabitListViewModelFactory(repository, context)
    )
    
    val habits by viewModel.habits.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadHabits()
    }
    
    when {
        showCreateHabit -> {
            val createViewModel = CreateHabitViewModel(existingHabit = editingHabit)
            CreateHabitView(
                viewModel = createViewModel,
                onSave = { habit ->
                    viewModel.saveHabit(habit)
                    showCreateHabit = false
                    editingHabit = null
                },
                onCancel = {
                    showCreateHabit = false
                    editingHabit = null
                }
            )
        }
        currentScreen == Screen.HabitDetail && selectedHabit != null -> {
            HabitDetailView(
                habit = selectedHabit!!,
                repository = repository,
                onBack = {
                    currentScreen = Screen.HabitList
                    selectedHabit = null
                }
            )
        }
        currentScreen == Screen.Dashboard -> {
            DashboardView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Statistics -> {
            StatisticsView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Achievements -> {
            val context = androidx.compose.ui.platform.LocalContext.current
            val gamificationService = remember { 
                com.rafaelmukhametov.habittrackerandroid.service.GamificationService(context)
            }
            val progress = remember { gamificationService.getUserProgress() }
            AchievementsView(
                achievements = emptyList(), // TODO: Add achievements list
                earnedBadges = progress.badgesEarned,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Challenges -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.ChallengesView(
                availableHabits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Templates -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.TemplatesView(
                onSelectTemplate = { template ->
                    // TODO: Create habit from template
                    currentScreen = Screen.HabitList
                },
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Groups -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.GroupsView(
                allHabits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Analytics -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.AnalyticsView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Insights -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.InsightsView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Comparison -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.ComparisonView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.WeeklyReview -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.WeeklyReviewView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Profile -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.ProfileView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Settings -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.SettingsView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.TimeOfDayStats -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.TimeOfDayStatsView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Triggers -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.TriggersView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.ExportOptions -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.ExportOptionsView(
                habit = null,
                allHabits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        currentScreen == Screen.Backup -> {
            com.rafaelmukhametov.habittrackerandroid.ui.screen.BackupView(
                habits = habits,
                onBack = { currentScreen = Screen.HabitList }
            )
        }
        else -> {
            HabitListView(
                viewModel = viewModel,
                onCreateHabitClick = {
                    editingHabit = null
                    showCreateHabit = true
                },
                onHabitClick = { habit ->
                    selectedHabit = habit
                    currentScreen = Screen.HabitDetail
                },
                onMenuClick = { screen ->
                    currentScreen = screen
                }
            )
        }
    }
}

sealed class Screen {
    object HabitList : Screen()
    object HabitDetail : Screen()
    object Dashboard : Screen()
    object Statistics : Screen()
    object Achievements : Screen()
    object Challenges : Screen()
    object Templates : Screen()
    object Groups : Screen()
    object Analytics : Screen()
    object Insights : Screen()
    object Comparison : Screen()
    object WeeklyReview : Screen()
    object Profile : Screen()
    object Settings : Screen()
    object TimeOfDayStats : Screen()
    object Triggers : Screen()
    object ExportOptions : Screen()
    object Backup : Screen()
}