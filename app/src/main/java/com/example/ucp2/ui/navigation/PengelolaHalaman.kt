package com.example.ucp2.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ucp2.ui.view.barang.DestinasiBarangInsert
import com.example.ucp2.ui.view.barang.DetailBarangView
import com.example.ucp2.ui.view.barang.HomeBarang
import com.example.ucp2.ui.view.barang.InsertBarangView
import com.example.ucp2.ui.view.barang.UpdateBarangView
import com.example.ucp2.ui.view.home.HomeScreen
import com.example.ucp2.ui.view.suplier.DestinasiSuplierInsert
import com.example.ucp2.ui.view.suplier.HomeSuplier
import com.example.ucp2.ui.view.suplier.InsertSuplierView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = DestinasiHome.route) {
        composable(route = DestinasiHome.route){
            HomeScreen(
                onBarangClick ={
                    navController.navigate(DestinasiBarangHome.route)
                },
                onSuplierClick = {
                    navController.navigate(DestinasiSuplierHome.route)
                },
                modifier = modifier
            )
        }

        composable(route = DestinasiBarangHome.route) {
            HomeBarang(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiBarangDetail.route}/$id")
                    println("PengelolaHalaman = $id")
                    Log.d("Cek Staut Detail" , "Id Detail : $id")
                },
                onBack = { navController.popBackStack() },
                onAddBarang = {
                    navController.navigate(DestinasiBarangInsert.route)
                },
                onEditClick = { id->
                    navController.navigate("${DestinasiBarangUpdate.route}/$id")
                    println("PengelolaHalaman = $id")
                    Log.d("CekStatusEdit", "Edit ID : $id")
                },
                modifier = modifier
            )
        }
//insert Barang
        composable(
            route = DestinasiBarangInsert.route
        ) {
            InsertBarangView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(route = DestinasiSuplierHome.route) {
            HomeSuplier(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiSuplierDetail.route}/$id")
                    println("PengelolaHalaman = $id")
                },
                onBack = { navController.popBackStack() },
                onAddSuplier = {
                    navController.navigate(DestinasiSuplierInsert.route)
                },
                modifier = modifier
            )
        }

        composable(
            DestinasiBarangDetail.routeWithArg,
            arguments = listOf(
                navArgument(DestinasiBarangDetail.ID) {
                    type = NavType.StringType
                }
            )
        ) {
            val ID = it.arguments?.getString(DestinasiBarangDetail.ID)
            Log.d("Composable", "ID diterima Detail: $ID")

            ID?.let { ID ->
                DetailBarangView(
                    onBack = {
                        navController.popBackStack()
                    },
                    modifier = modifier
                )
            }
        }

        composable(
            DestinasiBarangUpdate.routeWithArg,
            arguments = listOf(
                navArgument(DestinasiBarangUpdate.ID) { type = NavType.StringType }
            )
        ) {
            // Ambil argument ID
            val ID = it.arguments?.getString(DestinasiBarangUpdate.ID)
            Log.d("Composable", "ID diterima: $ID")
            ID?.let { ID ->
                UpdateBarangView(
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() },
                    modifier = modifier
                )
            }
        }




//insert Suplier
        composable(
            route = DestinasiSuplierInsert.route
        ) {
            InsertSuplierView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() },
                modifier = modifier
            )
        }


    }
}