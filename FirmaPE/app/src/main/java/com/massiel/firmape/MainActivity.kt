package com.massiel.firmape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.ui.screen.login.LoginScreen
import com.massiel.firmape.ui.screen.home.HomeScreen
import com.massiel.firmape.ui.screen.docs.DocsScreen
import com.massiel.firmape.ui.theme.FirmaPETheme

sealed interface Routes {
    data object Login: Routes
    data class Home(val user:Usuario): Routes
    data class Docs(val user:Usuario, val estado:String?): Routes
}

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FirmaPETheme { AppNav() } }
    }
}

@Composable
fun AppNav() {
    val nav: NavHostController = rememberNavController()
    var user by remember { mutableStateOf<Usuario?>(null) }

    NavHost(navController = nav, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onSuccess = { u -> user = u; nav.navigate("home") { popUpTo("login"){ inclusive=true } } }
            )
        }
        composable("home") {
            val u = user ?: return@composable
            HomeScreen(
                user = u,
                onGoDocs = { estado -> nav.navigate("docs/${estado ?: "ALL"}") }
            )
        }
        composable("docs/{estado}") { backStack ->
            val estadoArg = backStack.arguments?.getString("estado")?.let { if (it=="ALL") null else it }
            val u = user ?: return@composable
            DocsScreen(user = u, estado = estadoArg)
        }
    }
}