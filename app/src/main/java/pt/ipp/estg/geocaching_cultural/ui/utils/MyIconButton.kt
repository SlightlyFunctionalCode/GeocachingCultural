package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.ipp.estg.geocaching_cultural.R

@Composable
fun MyIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        contentPadding = PaddingValues(2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription =  stringResource(R.string.open_login_icons)
        )
    }
}