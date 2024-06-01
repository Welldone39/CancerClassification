package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.gesture.Prediction
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.AdapterNews
import com.dicoding.asclepius.data.entity.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.model.ViewModelFactory
import com.dicoding.asclepius.model.ViewModelMain
import com.dicoding.asclepius.utils.Number
import com.yalantis.ucrop.UCrop
import okhttp3.internal.wait
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var clasiffierImageHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val viewModelMain = viewModelObtain(this)

        clasiffierImageHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                   runOnUiThread {
                       binding.progressIndicator.visibility = View.GONE
                       results?.let {
                            val category = it[0].categories[0].label
                           val confidence = it[0].categories[0].score

                           currentImageUri?.let { uri ->
                               this@MainActivity.contentResolver.openInputStream(
                                   uri
                               )?.use { it .buffered().readBytes() }
                           }
                               ?.let { image ->
                                   val history = History (
                                       image = image,
                                       category = category,
                                       score = confidence
                                   )
                                    viewModelMain.createHistory(history)

                               }
                           moveToResult(
                               "Category: $category, Confindance: ${
                                Number.decimalToPercentage(
                                    confidence
                                )
                           }"
                           )
                       }
                   }
                }

                override fun onError(message: String) {
                    runOnUiThread {
                        binding.progressIndicator.visibility = View.GONE
                        showToast(message)
                    }
                }

            }
        )

        viewModelMain.news.observe(this) {
            binding.newsList.layoutManager = LinearLayoutManager(this)
            binding.newsList.adapter = AdapterNews().apply { submitList(it) }
        }

        viewModelMain.isLoading.observe(this) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModelMain.isError.observe(this) {
            if (it) showToast(getString(R.string.unable_to_load_news))
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> Intent(
                this,
                HistoryActivity::class.java
            ).also { startActivity(it) }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("temp_image.jpg")))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(224, 224)
                .start(this)
        } else {
            Log.d("Photo Picker", "No media Selected")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError: Throwable? =UCrop.getError(data!!)
            Log.e("Crop Error", "onActivityResult: ", cropError)
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri != null){
            binding.progressIndicator.visibility = View.VISIBLE
            currentImageUri?.let { clasiffierImageHelper.classifyStaticImage(it) }
        } else {
            showToast(getString(R.string.select_an_image_first))
        }
    }

    private fun moveToResult(prediction: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, prediction)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun viewModelObtain(activity: AppCompatActivity): ViewModelMain {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ViewModelMain::class.java]

    }
}