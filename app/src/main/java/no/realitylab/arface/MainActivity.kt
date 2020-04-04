package no.realitylab.arface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        button_makeup.setOnClickListener {
            startActivity(Intent(this, MakeupActivity::class.java))
        }

        button_glasses.setOnClickListener {
            startActivity(Intent(this, GlassesActivity::class.java))
        }

        button_regions.setOnClickListener {
            startActivity(Intent(this, FaceRegionsActivity::class.java))
        }

        button_face_landmarks.setOnClickListener {
            startActivity(Intent(this, FaceLandmarksActivity::class.java))
        }
    }

}
