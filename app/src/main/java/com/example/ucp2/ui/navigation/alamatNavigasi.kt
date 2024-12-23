package com.example.ucp2.ui.navigation

interface Alamatnavigasi {
    val route: String
}


object DestinasiHome : Alamatnavigasi{
    override val route = "home"
}
object DestinasiBarangHome : Alamatnavigasi {
    override val route = "barangHome"
}

object DestinasiBarangDetail : Alamatnavigasi {
    override val route = "DetailBarang"
    const val ID = "id"
    val routeWithArg = "$route/{$ID}"
}



object DestinasiBarangUpdate : Alamatnavigasi{
    override val route = "updateBarang"
    const val ID = "ID"
    val routeWithArg = "$route/{$ID}"
}

object DestinasiSuplierHome : Alamatnavigasi {
    override val route = "suplier"
}

object DestinasiSuplierDetail : Alamatnavigasi {
    override val route = "detailSuplier"
    const val ID = "ID"
    val routeWithArg = "$route/{$ID}"
}