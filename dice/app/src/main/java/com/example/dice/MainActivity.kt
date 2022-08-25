package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener {
            rollDice()
        }
        rollDice()
    }

    //instead of directly creating the object of class dice we just create
    //a function that contains everything and just call it
    /**
     * roll the dice and update the screen with result
     */
    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()
        //find image view in the layout
        val diceImage: ImageView = findViewById(R.id.imageView)
        //determine which drawable id to use when the dice is rolled
        val drawableResource = when (diceRoll){
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            1 -> R.drawable.dice_1
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        //updating the image acc to number
        diceImage.setImageResource(drawableResource)
        //content description
        diceImage.contentDescription = diceRoll.toString()

    }

    class Dice(private val numSides: Int) {

        fun roll(): Int {
            return (1..numSides).random()
        }
    }
}
