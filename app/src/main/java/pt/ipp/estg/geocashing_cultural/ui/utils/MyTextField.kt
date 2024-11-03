package pt.ipp.estg.geocashing_cultural.ui.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.geocashing_cultural.ui.theme.DarkGray
import pt.ipp.estg.geocashing_cultural.ui.theme.Gray
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray
import pt.ipp.estg.geocashing_cultural.ui.theme.White

@Composable
fun MyTextField(
    value: String,
    placeholder: @Composable() (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        placeholder = placeholder,
        textStyle = TextStyle(
            fontSize = 12.sp,
        ),
        colors = TextFieldDefaults.colors(
            cursorColor = DarkGray,
            focusedTextColor = White,
            unfocusedTextColor = LightGray,
            disabledTextColor = White,
            focusedContainerColor = LightGray,
            unfocusedContainerColor = White,
            disabledContainerColor = LightGray,
            focusedIndicatorColor = DarkGray,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPrefixColor = DarkGray,
            unfocusedPrefixColor = Color.Transparent,
            focusedSuffixColor = DarkGray,
            unfocusedSuffixColor = Color.Transparent,
            focusedTrailingIconColor = DarkGray,
            unfocusedTrailingIconColor = Color.Transparent,
            focusedLeadingIconColor = DarkGray,
            unfocusedLeadingIconColor = Color.Transparent,
            unfocusedPlaceholderColor = LightGray,
            selectionColors = TextSelectionColors(
                handleColor = DarkGray,
                backgroundColor = LightGray
            ),
        ),
        modifier = modifier.height(48.dp)
    )
}