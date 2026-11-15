package com.github.gszp1.carassistant.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private val ScreenPadding = 24.dp
private val SectionSpacing = 16.dp
private val CardSpacing = 12.dp
private val CardPadding = 16.dp
private val CardContentSpacing = 8.dp
private val CardCorner = 20.dp
private val SkipSectionSpacing = 8.dp

@Composable
fun PermissionsScreen(
    permissions: List<AppPermission>,
    onRequestPermission: (AppPermission) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        Text(
            text = "Permissions needed",
            style = MaterialTheme.typography.headlineLarge,
            color = palette.barLabel,
        )
        Text(
            text = "Car Assistant requires following permissions to provide full functionality.",
            style = MaterialTheme.typography.bodyLarge,
            color = palette.barLabel,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(CardSpacing),
        ) {
            items(permissions, key = { it.id }) { permission ->
                PermissionCard(
                    permission = permission,
                    onEnable = { onRequestPermission(permission) },
                    palette = palette,
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(SkipSectionSpacing)) {
            Text(
                text = "You can skip this for now – Car Assistant will ask again the moment " +
                    "a feature that needs it is used.",
                style = MaterialTheme.typography.bodySmall,
                color = palette.barLabel,
            )
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.barLabel),
            ) {
                Text("Continue without granting")
            }
        }
    }
}

@Composable
private fun PermissionCard(
    permission: AppPermission,
    onEnable: () -> Unit,
    palette: DrivePalette,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CardCorner),
        color = palette.contentCenter,
        contentColor = palette.barLabel,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CardPadding),
            verticalArrangement = Arrangement.spacedBy(CardContentSpacing),
        ) {
            Text(text = permission.title, style = MaterialTheme.typography.titleMedium)
            Text(text = permission.rationale, style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = onEnable,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.barCenter,
                    contentColor = palette.onAccent,
                ),
            ) {
                Text("Enable")
            }
        }
    }
}
