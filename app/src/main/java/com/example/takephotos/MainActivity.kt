package com.example.takephotos

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.takephotos.databinding.ActivityMainBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actualImage: ShapeableImageView

    private var imageNames: Array<ShapeableImageView?> = arrayOf(null,null,null,null)

    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = Firebase.storage

        permission.launch(android.Manifest.permission.CAMERA)

        binding.imageOne.setOnClickListener {
            actualImage = binding.imageOne
            imageNames.set(0,actualImage)
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageTwo.setOnClickListener {
            actualImage = binding.imageTwo
            imageNames.set(1,actualImage)
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageThree.setOnClickListener {
            actualImage = binding.imageThree
            imageNames.set(2,actualImage)
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }
        binding.imageFour.setOnClickListener {
            actualImage = binding.imageFour
            imageNames.set(3,actualImage)
            startForResult.launch(Intent(this, CameraPreviewActivity::class.java))
        }

        binding.uploadImage.setOnClickListener {
            uploadImage()
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

    private fun uploadImage()
    {
        var count = 0;
            for (item in imageNames ) {
                if (item != null) {
                    val storageRef = storage.reference

                    val newRef = storageRef.child("image$count.jpg")

                    val newImagesRef = storageRef.child("images/$newRef")

                    newRef.name == newImagesRef.name
                    newRef.path == newImagesRef.path

                    val bitmap = (item.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    var uploadTask = newImagesRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Snackbar.make(
                            binding.root,
                            "Erro ao inserir imagem",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        println("reference" + taskSnapshot.metadata?.reference)
                    }
                }
                count++
            }
    }
}