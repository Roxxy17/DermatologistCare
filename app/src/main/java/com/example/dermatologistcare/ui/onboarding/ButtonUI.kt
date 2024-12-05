package com.example.dermatologistcare.ui.onboarding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologistcare.ui.theme.DermatologistCareTheme
import com.example.dermatologistcare.ui.theme.coolveticaFontFamily

@Composable
fun ButtonUi(
    text: String = "NEXT",
    backgroundColor: Color =MaterialTheme.colorScheme.tertiary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontSize: Int = 14,
    modifier: Modifier = Modifier
,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor
        ), shape = RoundedCornerShape(10.dp),
                modifier = modifier
    ) {


        Text(
            text = text, fontSize = fontSize.sp, style = textStyle,
            fontFamily = coolveticaFontFamily,
            fontWeight = FontWeight.Normal
        )


    }


}


@Preview
@Composable
fun NextButton() {

    ButtonUi (text = "Next") {

    }



}

@Preview
@Composable
fun BackButton() {

    ButtonUi(text = "Back",
        backgroundColor = Color.Transparent,
        textColor = Color.Gray,
        textStyle = MaterialTheme.typography.bodySmall,
        fontSize = 13) {

}



}