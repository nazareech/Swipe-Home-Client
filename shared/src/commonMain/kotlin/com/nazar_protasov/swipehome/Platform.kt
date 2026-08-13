package com.nazar_protasov.swipehome

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform