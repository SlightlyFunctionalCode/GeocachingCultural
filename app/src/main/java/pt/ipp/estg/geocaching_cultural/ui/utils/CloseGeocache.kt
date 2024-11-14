package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.Green

@Composable
fun CloseGeocache(hint: String, distance: String, icon: Painter, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Green, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(end = 40.dp)) {
            Text(hint, modifier = Modifier.padding(15.dp))
            Text(distance, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Icon(
            painter = icon,
            contentDescription =  stringResource(R.string.show_categories_icon),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickable { onClick() }
        )
    }
}