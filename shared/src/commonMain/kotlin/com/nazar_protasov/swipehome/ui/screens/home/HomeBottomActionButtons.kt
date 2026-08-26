package com.nazar_protasov.swipehome.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.ic_dislike
import mymultiplatformproject.shared.generated.resources.ic_heart
import org.jetbrains.compose.resources.painterResource



@Composable
fun HomeBottomActionButtons(
    onDislikeClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 16.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка Dislike (Пропустити)
        Button(
            onClick = onDislikeClick,
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(painterResource(Res.drawable.ic_dislike), contentDescription = null, modifier = Modifier.size(32.dp))
        }

        // Кнопка Like (В обране)
        Button(
            onClick = onLikeClick,
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(painterResource(Res.drawable.ic_heart), contentDescription = null, modifier = Modifier.size(32.dp))
        }
    }
}