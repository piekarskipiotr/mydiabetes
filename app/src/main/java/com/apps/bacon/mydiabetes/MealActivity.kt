package com.apps.bacon.mydiabetes

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ImageAdapter
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.Image
import com.apps.bacon.mydiabetes.data.Meal
import com.apps.bacon.mydiabetes.databinding.*
import com.apps.bacon.mydiabetes.viewmodel.ImageViewModel
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class MealActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener, ImageAdapter.OnImageClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: ActivityMealBinding
    private lateinit var meal: Meal
    private val mealViewModel: MealViewModel by viewModels()
    private val imageViewModel: ImageViewModel by viewModels()
    @Inject
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mealId = intent.getIntExtra("MEAL_ID", -1)
        meal = mealViewModel.getMeal(mealId)

        setMealInfo()

        initProductsRecyclerView()
        initPhotosRecyclerView()

        binding.mealName.setOnClickListener {
            intent = Intent(this, ChangeMealNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MEAL_NAME)
        }

        val bottomSheetDialogCameraViewBinding = DialogAddImageBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        binding.takePhotoButton.setOnClickListener {
            bottomSheetDialog.setContentView(bottomSheetDialogCameraViewBinding.root)
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.show()

            bottomSheetDialogCameraViewBinding.cameraButton.setOnClickListener {
                intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_GET_IMAGE)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialogCameraViewBinding.galleryButton.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK)
                gallery.type = "image/*"

                startActivityForResult(gallery, REQUEST_CODE_GET_IMAGE_FROM_GALLERY)
                bottomSheetDialog.dismiss()
            }
        }

        binding.moreButton.setOnClickListener {
            moreDialog()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun moreDialog(){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogMoreBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.exportButton.setOnClickListener {
            dialogExport()
        }

        dialogBinding.deleteButton.setOnClickListener {
            dialogDeleteMeal()
        }

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun dialogExport(){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogExportBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.exportButton.setOnClickListener {
            val mealReference = database.child("Meal")
            mealReference.child(meal.name).setValue(meal)

            val pmjReference = database.child("PMJ")
            val pmJoinList = mealViewModel.getPMJbyMealId(meal.id)
            pmjReference.child(meal.name).setValue(pmJoinList)
        }

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun dialogDeleteMeal(){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteMealBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.mealName.text = meal.name

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            mealViewModel.deletePMJoin(meal.id)
            imageViewModel.getImageByMealId(meal.id).observe(this, {
                for(img in it){
                    imageViewModel.delete(img)
                }
            })
            mealViewModel.delete(meal)
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    private fun setMealInfo(){
        binding.mealName.text = meal.name

        mealViewModel.getProductsForMeal(meal.id).observe(this, {
            productsAdapter.updateData(it)
        })

        imageViewModel.getImageByMealId(meal.id).observe(this, {
            imageAdapter.updateData(it)
        })

        pieChart(meal.carbohydrateExchangers, meal.proteinFatExchangers, meal.calories)
    }

    private fun pieChart(
        carbohydrateExchangers: Double,
        proteinFatExchangers: Double,
        calories: Double
    ) {
        val pieChart: PieChart = binding.pieChart
        val data = ArrayList<PieEntry>()
        if (carbohydrateExchangers != 0.0)
            data.add(
                PieEntry(
                    carbohydrateExchangers.toFloat(),
                    resources.getString(R.string.pie_label_carbohydrate)
                )
            )
        if (proteinFatExchangers != 0.0)
            data.add(
                PieEntry(
                    proteinFatExchangers.toFloat(),
                    resources.getString(R.string.pie_label_protein_fat)
                )
            )

        val dataSet = PieDataSet(data, "")
        dataSet.setColors(
            ContextCompat.getColor(this, R.color.strong_yellow),
            ContextCompat.getColor(this, R.color.blue_purple)
        )

        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(DefaultValueFormatter(1))

        pieChart.data = pieData
        pieChart.centerText = "$calories\n${resources.getString(R.string.calories_value)}"
        pieChart.description.isEnabled = false

        pieChart.legend.textColor = ContextCompat.getColor(this, R.color.independent)

        pieChart.setDrawEntryLabels(false)

        pieChart.rotationAngle = 50f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.animate()
    }

    private fun initProductsRecyclerView(){
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter(this@MealActivity)
            adapter = productsAdapter

        }
    }

    private fun initPhotosRecyclerView(){
        binding.photosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            imageAdapter = ImageAdapter(this@MealActivity)
            adapter = imageAdapter

        }
    }

    private fun dialogImageManager(image: Image) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogManagerImageBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.imagePreview.setImageURI(Uri.parse(image.image))

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            meal.icon = null
            mealViewModel.update(meal)
            imageViewModel.delete(image)

            alertDialog.dismiss()
        }

        dialogBinding.setAsIconButton.setOnClickListener {
            meal.icon = image.image
            mealViewModel.update(meal)
            alertDialog.dismiss()

        }


        alertDialog.show()
    }

    override fun onProductClick(productId: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    private fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        while (true) {
            val len = inputStream.read(buffer)
            if (len != -1)
                byteBuffer.write(buffer, 0, len)
            else
                break
        }
        return byteBuffer.toByteArray()
    }

    override fun onImageLongClick(image: Image) {
        dialogImageManager(image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_MEAL_NAME -> {
                when(resultCode) {
                    RESULT_OK -> {
                        data?.let {
                            meal.name = it.getStringExtra("MEAL_NAME") as String
                            binding.mealName.text = meal.name

                            mealViewModel.update(meal)
                        }
                    }
                }
            }
            REQUEST_CODE_GET_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val imageUri = it.getStringExtra("IMAGE_URI").toString()
                        imageViewModel.insert(
                            Image(0, null, meal.id, imageUri)
                        )
                    }
                }
            }

            REQUEST_CODE_GET_IMAGE_FROM_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val photoFile = File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}.jpg"
                        )
                        photoFile.createNewFile()
                        val out = FileOutputStream(photoFile)
                        out.write(
                            getBytes(this.contentResolver.openInputStream(it.data!!)!!)
                        )
                        out.close()

                        imageViewModel.insert(
                            Image(0, null, meal.id, Uri.fromFile(photoFile).toString())
                        )
                    }
                }
            }
        }
    }

    companion object{
        private const val REQUEST_CODE_GET_IMAGE = 4
        private const val REQUEST_CODE_GET_IMAGE_FROM_GALLERY = 5
        private const val REQUEST_CODE_MEAL_NAME = 6
    }
}