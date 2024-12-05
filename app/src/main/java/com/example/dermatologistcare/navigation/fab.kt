package com.example.dermatologistcare.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dermatologistcare.R
import com.example.dermatologistcare.ui.camera.CameraActivity
import com.example.dermatologistcare.ui.camera.ResultActivity

@Composable
fun SubtractedNavigationShape(
    fabSize: Dp,
    cornerRadius: Dp
): Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val fabSizePx = with(density) { fabSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val fabRadiusPx = fabSizePx / 2f
        val additionalSpacing = 16f // Tambahan jarak di sekitar FAB
        val concaveDepthPx = fabRadiusPx / 1.5f

        return Outline.Generic(
            Path().apply {
                // Start dari top-left
                moveTo(0f, cornerRadiusPx)

                // Top-left corner
                quadraticBezierTo(0f, 0f, cornerRadiusPx, 0f)

                // Line ke start of cekungan
                lineTo(size.width / 2 - fabRadiusPx - additionalSpacing - cornerRadiusPx, 0f)

                // Cekungan kiri
                quadraticBezierTo(
                    size.width / 2 - fabRadiusPx - additionalSpacing, 0f,
                    size.width / 2 - fabRadiusPx - additionalSpacing, concaveDepthPx
                )

                // Cekungan bawah
                cubicTo(
                    size.width / 2 - fabRadiusPx - additionalSpacing, concaveDepthPx + fabRadiusPx,
                    size.width / 2 + fabRadiusPx + additionalSpacing, concaveDepthPx + fabRadiusPx,
                    size.width / 2 + fabRadiusPx + additionalSpacing, concaveDepthPx
                )

                // Cekungan kanan
                quadraticBezierTo(
                    size.width / 2 + fabRadiusPx + additionalSpacing, 0f,
                    size.width / 2 + fabRadiusPx + additionalSpacing + cornerRadiusPx, 0f
                )

                // Line ke top-right corner
                lineTo(size.width - cornerRadiusPx, 0f)

                // Top-right corner
                quadraticBezierTo(size.width, 0f, size.width, cornerRadiusPx)

                // Right side
                lineTo(size.width, size.height)

                // Bottom side
                lineTo(0f, size.height)

                // Left side
                lineTo(0f, cornerRadiusPx)

                close()
            }
        )
    }
}


@Composable
fun LiquidFabMenu(
    navController: NavController,
    isFabMenuExpanded: Boolean,
    onToggleMenu: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val fabSize = 72.dp
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Create an intent to start ResultActivity with the gallery image URI
            val intent = Intent(context, ResultActivity::class.java).apply {
                putExtra(CameraActivity.EXTRA_CAMERAX_IMAGE, uri.toString())
            }
            context.startActivity(intent)
        }
    }

    // Spring-based animation for FAB expansion
    val fabAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Animate FAB positions with spring
    val galleryFabOffset by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) -150f else 0f,
        animationSpec = fabAnimationSpec
    )

    val cameraFabOffset by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 150f else 0f,
        animationSpec = fabAnimationSpec
    )

    // Scale animation for FABs
    val galleryFabScale by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 1f else 0f,
        animationSpec = fabAnimationSpec
    )

    val cameraFabScale by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 1f else 0f,
        animationSpec = fabAnimationSpec
    )
    // Rotation animation for the main FAB
    var rotationAngle by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(fabSize)
    ) {
        // Gallery FAB
        FloatingActionButton(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .size(fabSize)
                .offset(y=(-fabSize / 1f),x =(fabSize / 1f))
                .scale(galleryFabScale)
                .align(Alignment.Center),
            containerColor = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = "Gallery",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Main FAB (Toggle Menu)
        FloatingActionButton(
            onClick = {
                onToggleMenu(!isFabMenuExpanded)
                rotationAngle += 90f // Rotate the icon by 45 degrees on click
            },
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.Center)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .rotate(rotation), // Apply the rotation
            containerColor = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = if (isFabMenuExpanded) R.drawable.ic_x else R.drawable.ic_scan),
                contentDescription = if (isFabMenuExpanded) "Close Menu" else "Open Menu",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        // Camera FAB
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Camera.route) },
            modifier = Modifier
                .size(fabSize)
                .offset(y=(-fabSize / 1f), x =(-fabSize / 1f))
                .scale(cameraFabScale)
                .align(Alignment.Center),
            containerColor = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Camera",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
