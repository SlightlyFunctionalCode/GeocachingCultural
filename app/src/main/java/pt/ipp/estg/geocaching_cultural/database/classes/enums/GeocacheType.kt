package pt.ipp.estg.geocaching_cultural.database.classes.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.ipp.estg.geocaching_cultural.R

enum class GeocacheType {
    GASTRONOMIA, CULTURAL, HISTORICO
}

@Composable
fun GeocacheType.getLocalizedName(): String {
    return when (this) {
        GeocacheType.GASTRONOMIA -> stringResource(R.string.gastronomy)
        GeocacheType.CULTURAL -> stringResource(R.string.cultural)
        GeocacheType.HISTORICO -> stringResource(R.string.historic)
    }
}
