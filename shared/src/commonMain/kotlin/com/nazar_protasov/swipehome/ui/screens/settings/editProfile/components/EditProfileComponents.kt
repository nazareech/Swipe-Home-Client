package com.nazar_protasov.swipehome.ui.screens.settings.editProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nazar_protasov.swipehome.ui.theme.SwipeHomeTheme
import mymultiplatformproject.shared.generated.resources.Res
import mymultiplatformproject.shared.generated.resources.Res.string
import mymultiplatformproject.shared.generated.resources.ic_arrow_back
import mymultiplatformproject.shared.generated.resources.ic_camera
import mymultiplatformproject.shared.generated.resources.ic_edit_pencil
import mymultiplatformproject.shared.generated.resources.ic_unlink_account
import mymultiplatformproject.shared.generated.resources.ic_user_verified
import mymultiplatformproject.shared.generated.resources.profile_tab_photo_avatar_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditProfileTopBar(onBackClicked: () -> Unit, onSaveClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SwipeHomeTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                painterResource(Res.drawable.ic_arrow_back),
                contentDescription = "Back"
            )
        }

        Text("Редагування", style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.neutral)

        TextButton(
            onClick = onSaveClick,
            shape = SwipeHomeTheme.shapes.mediumShape
        ){
            Text("Зберегти", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.primary)
        }
    }
}

@Composable
fun EditableProfileAvatar(onClick:() -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Аватарка з градієнтною рамкою редагувати
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = "http://",
                contentDescription = stringResource(string.profile_tab_photo_avatar_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                SwipeHomeTheme.colors.primary,
                                SwipeHomeTheme.colors.neutral
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Кнопка редагування (олівець)
            Box(
                modifier = Modifier
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SwipeHomeTheme.colors.background)
                    .border(2.dp, SwipeHomeTheme.colors.primary, CircleShape)
                    .clickable { /*TODO Змінити фото*/ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(Res.drawable.ic_edit_pencil),
                    contentDescription = stringResource(string.profile_tab_photo_avatar_description),
                    tint = SwipeHomeTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = SwipeHomeTheme.shapes.mediumShape,
        colors = CardDefaults.cardColors(containerColor = SwipeHomeTheme.colors.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ){
        Column(modifier = Modifier.padding(vertical = 16.dp)){
            content()
        }
    }
}

@Composable
fun SectionTitle(icon: Painter, title: String, tint: Color){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)){
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = SwipeHomeTheme.typography.label, color = SwipeHomeTheme.colors.onSurfaceSecondary)
    }
}

@Composable
fun CustomTextFiles(label: String, value: String, onValueChange: (String) -> Unit, bgColor: Color){
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(label, style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = SwipeHomeTheme.shapes.mediumShape
        )
    }
}

@Composable
fun TipBanner (icon: Painter, text: String, bgColor: Color, textColor: Color){
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp).clip(SwipeHomeTheme.shapes.mediumShape).background(bgColor).padding(16.dp),
        verticalAlignment = Alignment.Top
    ){
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = SwipeHomeTheme.typography.caption, color = textColor, lineHeight = 18.sp)
    }
}

@Composable
fun LinkedAccountItem(icon: Painter, name: String, details: String, isLinked: Boolean, bgColor: Color){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clip(SwipeHomeTheme.shapes.mediumShape).background(bgColor).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(SwipeHomeTheme.colors.surface), contentAlignment = Alignment.Center) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        Column {
            Text(name, style = SwipeHomeTheme.typography.label, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onSurfaceSecondary)
            Text(details, style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.onSurfaceSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.widthIn(130.dp))
        }

        if(isLinked){
            Row(modifier = Modifier.clickable{ /*TODO: */ }, verticalAlignment = Alignment.CenterVertically){
                Icon(painterResource(Res.drawable.ic_unlink_account), contentDescription = null, tint = SwipeHomeTheme.colors.secondary)
                Spacer(Modifier.width(4.dp))
                Text("Відв'язати", style = SwipeHomeTheme.typography.caption, color = SwipeHomeTheme.colors.error)
            }
        }else{
            Box(modifier = Modifier.clip(SwipeHomeTheme.shapes.mediumShape).background(SwipeHomeTheme.colors.primary).clickable{ /*TODO: */ }.padding(horizontal = 12.dp, vertical = 6.dp)){
                Text("Прив'язати", style = SwipeHomeTheme.typography.caption, fontWeight = FontWeight.Bold, color = SwipeHomeTheme.colors.onPrimary)
            }
        }

    }
}

@Composable
fun PhotoBottomSheetContent(onDismiss: () -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Змінити світлину профілю", fontWeight = FontWeight.Bold,  style = SwipeHomeTheme.typography.subheadline, color = SwipeHomeTheme.colors.onSurfaceSecondary)
        Spacer(Modifier.height(24.dp))

        BottomSheetButton(painterResource(Res.drawable.ic_camera), "Зробити нове фото", SwipeHomeTheme.colors.primary, SwipeHomeTheme.colors.onPrimary, onClick = {/*TODO*/})
        Spacer(Modifier.height(12.dp))

        BottomSheetButton(painterResource(Res.drawable.ic_camera), "Вибрати з галереї", SwipeHomeTheme.colors.primary, SwipeHomeTheme.colors.onPrimary, onClick = {/*TODO*/})
        Spacer(Modifier.height(12.dp))

        BottomSheetButton(painterResource(Res.drawable.ic_camera), "Видалити фото", SwipeHomeTheme.colors.primary, SwipeHomeTheme.colors.onPrimary, onClick = {/*TODO*/})
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SwipeHomeTheme.colors.primary,
                contentColor = SwipeHomeTheme.colors.onPrimary
            ),
            shape = SwipeHomeTheme.shapes.mediumShape
        ){
            Text("Скасувати", fontWeight = FontWeight.Bold, style = SwipeHomeTheme.typography.body)
        }
    }
}

@Composable
fun BottomSheetButton(icon: Painter, text: String, bgColor: Color, contentColor: Color, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp)
            .clip(SwipeHomeTheme.shapes.mediumShape)
            .background(bgColor)
            .clickable{ onClick() }
            .padding(horizontal =  16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(icon, contentDescription = null, tint = contentColor)
        Spacer(Modifier.width(16.dp))
        Text(text, style = SwipeHomeTheme.typography.body, color = contentColor)
    }
}

@Composable
fun IsConfirmData(){
    Spacer(Modifier.height(6.dp))

    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = SwipeHomeTheme.colors.primary,
            contentColor = SwipeHomeTheme.colors.onPrimary
        ),
        shape = SwipeHomeTheme.shapes.mediumShape,
        modifier = Modifier.fillMaxWidth().height(56.dp).clip(SwipeHomeTheme.shapes.mediumShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(Res.drawable.ic_user_verified),
                contentDescription = null,
                tint = SwipeHomeTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Підтверджено",
                style = SwipeHomeTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = SwipeHomeTheme.colors.onPrimary
            )
        }
    }
}