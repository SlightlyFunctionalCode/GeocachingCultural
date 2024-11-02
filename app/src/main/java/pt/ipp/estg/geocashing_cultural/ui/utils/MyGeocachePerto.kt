package pt.ipp.estg.geocashing_cultural.ui.utils

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
import androidx.compose.ui.unit.dp
import pt.ipp.estg.geocashing_cultural.ui.theme.Green

@Composable
fun MyGeocachePertoDeMim(titulo : String, distancia : String, icone: Painter){
    Box(modifier = Modifier
        .background(color = Green, shape = RoundedCornerShape(10.dp))
        .fillMaxWidth()) {

        // Conteúdo principal
        Column(modifier = Modifier.padding(end = 40.dp)) { // Adiciona um padding para o espaço do ícone
            Text(titulo, modifier = Modifier.padding(15.dp))
            Text(distancia, modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Ícone no canto superior direito
        Icon(
            painter = icone, // Substitua pelo seu ícone
            contentDescription = "Ícone",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp) // Ajuste o padding conforme necessário
                .size(24.dp) // Ajuste o tamanho do ícone conforme necessário
                .clickable { /* Ação ao clicar no ícone */ }
        )
    }
}