package com.nazar_protasov.swipehome.ui.screens.home.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.nazar_protasov.swipehome.network.PropertyApiService
import com.nazar_protasov.swipehome.ui.screens.home.main.HomeScreenModel
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.filter_building_all
import mymultiplatformproject.shared.generated.resources.filter_building_apartment
import mymultiplatformproject.shared.generated.resources.filter_building_house
import mymultiplatformproject.shared.generated.resources.filter_building_khrushchovka
import mymultiplatformproject.shared.generated.resources.filter_building_new
import mymultiplatformproject.shared.generated.resources.filter_building_room
import mymultiplatformproject.shared.generated.resources.filter_deal_all
import mymultiplatformproject.shared.generated.resources.filter_deal_rent
import mymultiplatformproject.shared.generated.resources.filter_deal_sale
import mymultiplatformproject.shared.generated.resources.filter_floor_first
import mymultiplatformproject.shared.generated.resources.filter_floor_last
import mymultiplatformproject.shared.generated.resources.filter_floor_middle
import mymultiplatformproject.shared.generated.resources.filter_parking_all
import mymultiplatformproject.shared.generated.resources.filter_parking_garage
import mymultiplatformproject.shared.generated.resources.filter_parking_secured
import mymultiplatformproject.shared.generated.resources.filter_parking_street
import mymultiplatformproject.shared.generated.resources.filter_property_apartment
import mymultiplatformproject.shared.generated.resources.filter_property_house
import mymultiplatformproject.shared.generated.resources.filter_property_rooms
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class PropertyType{
    ALL {
        @Composable
        override fun getLabel(): String {
            return stringResource(Res.string.filter_deal_all)
        }
    },
    APARTMENT {
        @Composable
        override fun getLabel(): String {
            return stringResource(Res.string.filter_property_apartment)
        }
    },
    HOUSE {
        @Composable
        override fun getLabel(): String {
            return stringResource(Res.string.filter_property_house)
        }
    },
    ROOM {
        @Composable
        override fun getLabel(): String {
            return stringResource(Res.string.filter_property_rooms)
        }
    };

    @Composable
    abstract fun getLabel() : String
}

enum class DealType{
    ALL {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_deal_all)
        }
    },
    RENT {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_deal_rent)
        }
    },
    SALE {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_deal_sale)
        }
    };

    @Composable
    abstract fun getName() : String
}

//enum class BuildingType{
//    ALL {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_all)
//        }
//    },
//    NEW {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_new)
//        }
//    },
//    HOUSE {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_house)
//        }
//    },
//    APARTMENT {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_apartment)
//        }
//    },
//    ROOM {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_room)
//        }
//    },
//    KHRUSHOWKA {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_khrushchovka)
//        }
//    };
//
//    @Composable
//    abstract fun getName() : String
//}

enum class Parking{
    ALL {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_parking_all)
        }
    },
    GARAGE {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_parking_garage)
        }
    },
    STREET {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_parking_street)
        }
    },
    SECURED {
        @Composable
        override fun getName(): String {
            return stringResource(Res.string.filter_parking_secured)
        }
    };

    @Composable
    abstract fun getName() : String
}

//enum class Floor{
//    ALL {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_building_all)
//        }
//    },
//    FIRST {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_floor_first)
//        }
//    },
//    MIDDLE {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_floor_middle)
//        }
//    },
//    LAST {
//        @Composable
//        override fun getName(): String {
//            return stringResource(Res.string.filter_floor_last)
//        }
//    };
//
//    @Composable
//    abstract fun getName() : String
//}


enum class BuildingType(val resource: StringResource) {
    ALL(Res.string.filter_building_all),
    NEW(Res.string.filter_building_new),
    HOUSE(Res.string.filter_building_house),
    APARTMENT(Res.string.filter_building_apartment),
    ROOM(Res.string.filter_building_room),
    KHRUSHOWKA(Res.string.filter_building_khrushchovka)
}

enum class Floor(val resource: StringResource) {
    ALL(Res.string.filter_building_all),
    FIRST(Res.string.filter_floor_first),
    MIDDLE(Res.string.filter_floor_middle),
    LAST(Res.string.filter_floor_last)
}

class FilterScreenModel(
    private val homeScreenModel: HomeScreenModel,
    private val apiService: PropertyApiService
) : ScreenModel {
    // Отримуємо поточні (раніше збережені) фільтри
    val activeFilters = homeScreenModel.currentFilters.value

    var selectedCategory by mutableStateOf(activeFilters.category)

    var dealType by mutableStateOf(activeFilters.dealType)

    var minPrice by mutableStateOf(activeFilters.priceMin?.toInt()?.toString())
    var maxPrice by mutableStateOf(activeFilters.priceMax?.toInt()?.toString())

    var minArea by mutableStateOf(activeFilters.areaMin?.toInt()?.toString())
    var maxArea by mutableStateOf(activeFilters.areaMax?.toInt()?.toString())

    var selectedRooms by mutableStateOf(activeFilters.rooms?.toString())

    var buildingType by mutableStateOf(activeFilters.buildingType)

    var selectedFloor by mutableStateOf(activeFilters.floor)

    var selectedParking by mutableStateOf(activeFilters.parking)

    // Зручності
    var petsAllowed by mutableStateOf(activeFilters.petsAllowed)
    var hasElevator by mutableStateOf(activeFilters.elevator)
    var withFurniture by mutableStateOf(activeFilters.furniture)
    var hasBalcony by mutableStateOf(activeFilters.balcony)



}