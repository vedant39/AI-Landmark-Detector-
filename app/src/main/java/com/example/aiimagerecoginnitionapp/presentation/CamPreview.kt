package com.example.aiimagerecoginnitionapp.presentation

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

@Composable
fun CamPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
){
    val lifecycle = LocalLifecycleOwner.current
    AndroidView(
        factory = { PreviewView(it).apply{
            this.controller = controller
            controller.bindToLifecycle(lifecycle as LifecycleOwner)

            }
        },
        modifier = modifier
    )
}