package com.massiel.firmape.ui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.ui.screen.admin.AdminScreen
import com.massiel.firmape.ui.screen.docs.DocsScreen
import com.massiel.firmape.ui.screen.docs.UploadLocalScreen
import com.massiel.firmape.ui.screen.login.LoginScreen
import com.massiel.firmape.ui.screen.home.HomeScreen
import com.massiel.firmape.ui.theme.FirmaPETheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirmaPETheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val nav: NavHostController = rememberNavController()
    var user by remember { mutableStateOf<Usuario?>(null) }

    NavHost(navController = nav, startDestination = "login") {

        // 🔹 Pantalla Login
        composable("login") {
            LoginScreen(
                onSuccess = { u ->
                    user = u
                    // Pasamos a Home y eliminamos el login del back stack
                    nav.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 🔹 Pantalla Home
        composable("home") {
            val u = user ?: return@composable
            HomeScreen(
                user = u,
                onGoDocs = { estado -> nav.navigate("docs/${estado ?: "ALL"}") },
                onGoUpload = { nav.navigate("uploadLocal") },
                onGoAdmin = { nav.navigate("admin") },// 👈 aquí navegamos al módulo administración
                onLogout = {
                    user = null
                    nav.navigate("login") { popUpTo("home") { inclusive = true } }
                }
            )
        }

        // 🔹 Pantalla Documentos
        composable(
            route = "docs/{estado}",
            arguments = listOf(
                navArgument("estado") { type = NavType.StringType }
            )
        ) { backStack ->
            val estadoArg = backStack.arguments?.getString("estado")?.let { if (it == "ALL") null else it }
            val u = user ?: return@composable
            DocsScreen(
                navController = nav,            // 👈 agrega esto
                estado = estadoArg,
                signerName = u.nombre,
                onUpload = { nav.navigate("uploadLocal") }
            )
        }

        // 🔹 Pantalla Subir Documento
        composable("uploadLocal") {
            UploadLocalScreen(
                onDone = { nav.popBackStack() }
            )
        }
        // 🔹 Pantalla Administración
        composable("admin") {
            AdminScreen(
                onBack = { nav.popBackStack() }
            )
        }

    }
}
