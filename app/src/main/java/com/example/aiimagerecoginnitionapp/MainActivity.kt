package com.example.aiimagerecoginnitionapp
import androidx.compose.material3.Text
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aiimagerecoginnitionapp.data.TFLiteLandmarkClassifier
import com.example.aiimagerecoginnitionapp.domain.Classification
import com.example.aiimagerecoginnitionapp.domain.LandmarkClassifier
import com.example.aiimagerecoginnitionapp.presentation.CamPreview
import com.example.aiimagerecoginnitionapp.presentation.ImageAnalyzer
import com.example.aiimagerecoginnitionapp.ui.theme.AIImageRecoginnitionAppTheme
import android.Manifest
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasCameraPermission()){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }

        setContent {
            AIImageRecoginnitionAppTheme {
                var classifications by remember {
                    mutableStateOf(emptyList<Classification>())
                }
                val analyzer = remember{
                    ImageAnalyzer(
                        classifier = TFLiteLandmarkClassifier(
                            context = applicationContext
                        ),
                        onResults = {
                            classifications = it
                        }
                    )
            }
                val controller = remember()
                {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    CamPreview(controller , Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxSize()
                    ) {
                        classifications.forEach{
                            androidx.compose.material3.Text( // Use fully qualified name
                                text = it.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(8.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp, // Correct fontsize to fontSize
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

}