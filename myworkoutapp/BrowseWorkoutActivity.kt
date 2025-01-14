package com.example.myworkoutapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import android.content.Intent
import com.example.myworkoutapp.data.ExerciseType


class BrowseWorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_workout)

        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish() 
        }

        val imageContainer: ConstraintLayout = findViewById(R.id.imageContainer)

        val imageResIds = getDrawableResourceIds()

        var previousId = View.generateViewId()
        imageResIds.forEachIndexed { index, resId ->
            val imageButton = ImageButton(this).apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(0, 0).apply {
                    dimensionRatio = "H,1:0.4"
                }
                setImageResource(resId)
                background = ContextCompat.getDrawable(context, android.R.color.transparent)
                scaleType = ImageView.ScaleType.CENTER_CROP
                contentDescription = "Image Button $index"
            }

            imageContainer.addView(imageButton)

            val constraintSet = ConstraintSet().apply {
                clone(imageContainer)
                if (index == 0) {
                    connect(imageButton.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16)
                } else {
                    connect(imageButton.id, ConstraintSet.TOP, previousId, ConstraintSet.BOTTOM, 16)
                }
                connect(imageButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 32) // Add margin to the start
                connect(imageButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 32) // Add margin to the end
                constrainWidth(imageButton.id, ConstraintSet.MATCH_CONSTRAINT)
                constrainHeight(imageButton.id, ConstraintSet.MATCH_CONSTRAINT)
                setDimensionRatio(imageButton.id, "H,1:0.4")
            }
            constraintSet.applyTo(imageContainer)

            imageButton.setOnClickListener {
                val exerciseType = when(index) {
                    0 -> ExerciseType.ARMS
                    1 -> ExerciseType.LEGS
                    2 -> ExerciseType.BACK
                    3 -> ExerciseType.CHEST
                    4 -> ExerciseType.TRICEPS
                    5 -> ExerciseType.BICEPS
                    6 -> ExerciseType.QUADS
                    7 -> ExerciseType.ABS
                    8 -> ExerciseType.CORE
                    else -> ExerciseType.ARMS
                }
                val intent = Intent(this, ExerciseDetailActivity::class.java).apply {
                    putExtra("EXERCISE_TYPE", exerciseType.searchTerm)
                }
                startActivity(intent)
            }

            previousId = imageButton.id
        }
    }


    private fun getDrawableResourceIds(): List<Int> {
        val prefix = "newworkout"
        val fields = R.drawable::class.java.declaredFields
        return fields.mapNotNull { field ->
            try {
                val resourceId = field.getInt(null)
                val resourceName = field.name
                if (resourceName.startsWith(prefix)) {
                    resourceId
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}