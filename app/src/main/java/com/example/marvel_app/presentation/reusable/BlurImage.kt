package com.example.marvel_app.presentation.reusable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BlurImageFromUrl(
    imageUrl: String,
    modifier: Modifier = Modifier,
    blurRadius: Float = 10f
) {
    val context = LocalContext.current
    val painter = rememberImagePainter(data = imageUrl)

    val originalBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val blurredBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(painter) {
        withContext(Dispatchers.IO) {
            val drawable = painter.imageLoader.execute(painter.request).drawable
            if (drawable is BitmapDrawable) {
                originalBitmap.value = drawable.bitmap

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    blurredBitmap.value = originalBitmap.value?.let { bitmap ->
                        blurBitmap(context, bitmap, blurRadius)
                    }
                } else {
                    blurredBitmap.value = originalBitmap.value
                }
            }
        }
    }

    blurredBitmap.value?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
    // Convert the bitmap to ARGB_8888 if it's in HARDWARE config
    val safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val outputBitmap = Bitmap.createBitmap(safeBitmap)

    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, safeBitmap)
    val output = Allocation.createFromBitmap(renderScript, outputBitmap)

    val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    blurScript.setRadius(radius)
    blurScript.setInput(input)
    blurScript.forEach(output)

    output.copyTo(outputBitmap)
    renderScript.destroy()

    return outputBitmap
}