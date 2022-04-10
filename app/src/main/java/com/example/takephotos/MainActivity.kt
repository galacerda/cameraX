package com.example.takephotos

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.takephotos.databinding.ActivityMainBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actualImage: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permission.launch(android.Manifest.permission.CAMERA)

        binding.imageOne.setOnClickListener {
            actualImage = binding.imageOne
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageTwo.setOnClickListener {
            actualImage = binding.imageTwo
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageThree.setOnClickListener {
            actualImage = binding.imageThree
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageFour.setOnClickListener {
            actualImage = binding.imageFour
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
    }

    private val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            Snackbar.make(
                binding.root,
                "Sem permissões para acessar a câmera",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){
                val uriImage = result.data?.data
                actualImage.setImageURI(uriImage)
            }
    }
}