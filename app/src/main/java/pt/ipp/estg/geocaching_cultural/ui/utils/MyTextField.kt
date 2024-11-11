package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.geocaching_cultural.ui.theme.Black
import pt.ipp.estg.geocaching_cultural.ui.theme.DarkGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Gray
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.White

@Composable
fun MyTextField(
    value: String,
    placeholder: @Composable() (() -> Unit)? = null,
    label: @Composable() (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable() (() -> Unit)? = null,
    supportingText: @Composable() (() -> Unit)? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = label,
        shape = RoundedCornerShape(10.dp),
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        textStyle = TextStyle(
            fontSize = 12.sp,
        ),
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedLabelColor = LightGray,
            focusedLabelColor = LightGray,
            errorLabelColor = Pink,
            errorContainerColor = White,
            errorBorderColor = Pink,
            errorTextColor = Black,
            errorSupportingTextColor = Pink,
            focusedBorderColor = Gray,
            unfocusedBorderColor = LightGray,
            cursorColor = Gray,
            focusedTextColor = Black,
            unfocusedTextColor = LightGray,
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedPrefixColor = Gray,
            unfocusedPrefixColor = Color.Transparent,
            focusedSuffixColor = Gray,
            unfocusedSuffixColor = Color.Transparent,
            focusedTrailingIconColor = Gray,
            unfocusedTrailingIconColor = LightGray,
            focusedLeadingIconColor = Gray,
            unfocusedLeadingIconColor = LightGray,
            unfocusedPlaceholderColor = LightGray,
            selectionColors = TextSelectionColors(
                handleColor = Gray,
                backgroundColor = LightGray
            )
        ),
        modifier = modifier
    )
}