package com.example.dermatologistcare.ui.camera

import android.app.Activity
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dermatologistcare.R
import com.example.dermatologistcare.navigation.Screen
import com.yalantis.ucrop.UCrop
import java.io.File

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

    // Launcher for cropping the image
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let {
                // Intent to start ResultActivity with the cropped image URI
                val intent = Intent(context, ResultActivity::class.java).apply {
                    putExtra(CameraActivity.EXTRA_CAMERAX_IMAGE, it.toString())
                }
                context.startActivity(intent)
            }
        }
    }

    // Launcher for gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_image.jpg"))

            val toolbarColor = Color(0xFFF5F5F5)
            val statusBarColor = Color(0xFF229799)
            val activeControlColor = Color(0xFF48CFCB)

            // Konfigurasi UCrop dengan warna dari hex
            val options = UCrop.Options().apply {
                setFreeStyleCropEnabled(true)
                setToolbarColor(toolbarColor.toArgb()) // Menggunakan warna hex yang dikonversi ke Argb
                setStatusBarColor(statusBarColor.toArgb()) // Menggunakan warna hex yang dikonversi ke Argb
                setActiveControlsWidgetColor(activeControlColor.toArgb()) // Menggunakan warna hex yang dikonversi ke Argb
            }
            val cropIntent = UCrop.of(it, destinationUri)
                .withOptions(options)
                .getIntent(context)
            cropLauncher.launch(cropIntent)
        }
    }

    // Spring-based animation for FAB expansion
    val fabAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val galleryFabOffset by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) -150f else 0f,
        animationSpec = fabAnimationSpec
    )
    val cameraFabOffset by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 150f else 0f,
        animationSpec = fabAnimationSpec
    )
    val galleryFabScale by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 1f else 0f,
        animationSpec = fabAnimationSpec
    )
    val cameraFabScale by animateFloatAsState(
        targetValue = if (isFabMenuExpanded) 1f else 0f,
        animationSpec = fabAnimationSpec
    )

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
                .offset(y = -fabSize / 1f, x = fabSize / 1f)
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
                rotationAngle += 90f
            },
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.Center)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .rotate(rotation),
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
                .offset(y = -fabSize / 1f, x = -fabSize / 1f)
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
