package com.nazar_protasov.swipehome.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.nazar_protasov.swipehome.ui.components.MenuItemData
import com.nazar_protasov.swipehome.ui.components.ProfileActions
import com.nazar_protasov.swipehome.ui.components.ProfileHeader
import com.nazar_protasov.swipehome.ui.components.ProfileMenuCard
import com.nazar_protasov.swipehome.ui.components.ProfileTopBar
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_certified_shield
import mymultiplatformproject.shared.generated.resources.ic_language
import mymultiplatformproject.shared.generated.resources.ic_list
import mymultiplatformproject.shared.generated.resources.ic_notifications_bell
import mymultiplatformproject.shared.generated.resources.ic_politics
import mymultiplatformproject.shared.generated.resources.ic_profile
import mymultiplatformproject.shared.generated.resources.ic_security
import mymultiplatformproject.shared.generated.resources.ic_support
import mymultiplatformproject.shared.generated.resources.profile_tab_conditions_of_use
import mymultiplatformproject.shared.generated.resources.profile_tab_edit_info
import mymultiplatformproject.shared.generated.resources.profile_tab_my_list_properties
import mymultiplatformproject.shared.generated.resources.profile_tab_name
import mymultiplatformproject.shared.generated.resources.profile_tab_policity_description
import mymultiplatformproject.shared.generated.resources.profile_tab_settings_language
import mymultiplatformproject.shared.generated.resources.profile_tab_settings_notifications
import mymultiplatformproject.shared.generated.resources.profile_tab_settings_support
import mymultiplatformproject.shared.generated.resources.profile_tab_support
import mymultiplatformproject.shared.generated.resources.profile_tab_verify_account
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ProfileTab : Tab {
    override val key = uniqueScreenKey

    override val options: TabOptions
        @Composable
        get(){
            val title = stringResource(Res.string.profile_tab_name)
            val icon = painterResource(Res.drawable.ic_profile)
            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen)
    }
}

internal object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SwipeHomeTheme.colors.background)
                .verticalScroll(scrollState)
        ) {
            ProfileTopBar()

            ProfileHeader()

            Spacer(modifier = Modifier.height(32.dp))

            ProfileMenuCard(
                items = listOf(
                    MenuItemData(painterResource(Res.drawable.ic_profile), stringResource(Res.string.profile_tab_edit_info)),
                    MenuItemData(painterResource(Res.drawable.ic_certified_shield), stringResource(Res.string.profile_tab_verify_account)),
                    MenuItemData(painterResource(Res.drawable.ic_list), stringResource(Res.string.profile_tab_my_list_properties), badgeCount = 12),
                    MenuItemData(painterResource(Res.drawable.ic_notifications_bell), stringResource(Res.string.profile_tab_settings_notifications)),
                    MenuItemData(painterResource(Res.drawable.ic_language), stringResource(Res.string.profile_tab_settings_language)),
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(Res.string.profile_tab_settings_support),
                style = SwipeHomeTheme.typography.caption,
                color = SwipeHomeTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileMenuCard(
                items = listOf(
                    MenuItemData(painterResource(Res.drawable.ic_support), stringResource(Res.string.profile_tab_support)),
                    MenuItemData(painterResource(Res.drawable.ic_politics), stringResource(Res.string.profile_tab_conditions_of_use)),
                    MenuItemData(painterResource(Res.drawable.ic_security), stringResource(Res.string.profile_tab_policity_description))
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileActions()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}