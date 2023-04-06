package com.example.mobilecw

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilecw.databinding.ActivityNewGameBinding
import kotlinx.coroutines.*
import java.util.*


class NewGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewGameBinding
    private val scope = CoroutineScope(Dispatchers.Default)
    //var maxScore : Int = 0 // Target score which the user updates at the start
    var playerScore : Int = 0 // Global var to store the user score
    var computerScore : Int = 0 // Global var to store the computer score
    var playerAttempt : Int = 0 // Global var to store the number of rounds/attempts for user
    var computerAttempt : Int = 0 // Global var to store the number of rounds/attempts for computer
    var humanTurn = true // Determines if the computer or user is playing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To start the game
        binding.throwBtn.setOnClickListener {
            diceRoller()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the state of the ImageView and TextView objects

        val playerscore=findViewById<TextView>(R.id.playerScore)
        val numplayer=findViewById<TextView>(R.id.numPlayer)

        val gamemsg=findViewById<TextView>(R.id.gameMsg)
        val select1=findViewById<TextView>(R.id.select1)
        val select2=findViewById<TextView>(R.id.select2)
        val select3=findViewById<TextView>(R.id.select3)
        val select4=findViewById<TextView>(R.id.select4)
        val select5=findViewById<TextView>(R.id.select5)

        val input=findViewById<EditText>(R.id.inputScore)

        outState.putInt("dice1", R.drawable.dice1)
        outState.putInt("dice2", R.drawable.dice2)
        outState.putInt("dice3", R.drawable.dice3)
        outState.putInt("dice4", R.drawable.dice4)
        outState.putInt("dice5", R.drawable.dice5)
        outState.putString("playerScore", playerscore.text.toString())
        outState.putString("numPlayer", numplayer.text.toString())
        //outState.putString("txtStart", textstart.text.toString())
        outState.putString("gameMsg", gamemsg.text.toString())
        outState.putString("select1", select1.text.toString())
        outState.putString("select2", select2.text.toString())
        outState.putString("select3", select3.text.toString())
        outState.putString("select4", select4.text.toString())
        outState.putString("select5", select5.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val diece1=findViewById<ImageView>(R.id.dice1)
        val diece2=findViewById<ImageView>(R.id.dice2)
        val diece3=findViewById<ImageView>(R.id.dice3)
        val diece4=findViewById<ImageView>(R.id.dice4)
        val diece5=findViewById<ImageView>(R.id.dice5)
        val playerscore=findViewById<TextView>(R.id.playerScore)
        val numplayer=findViewById<TextView>(R.id.numPlayer)

        val gamemsg=findViewById<TextView>(R.id.gameMsg)
        val select1=findViewById<TextView>(R.id.select1)
        val select2=findViewById<TextView>(R.id.select2)
        val select3=findViewById<TextView>(R.id.select3)
        val select4=findViewById<TextView>(R.id.select4)
        val select5=findViewById<TextView>(R.id.select5)

        val input=findViewById<EditText>(R.id.inputScore)

        // Restore the state of the ImageView and TextView objects
        diece1.setImageResource(savedInstanceState.getInt("dice1"))
        diece2.setImageResource(savedInstanceState.getInt("dice2"))
        diece3.setImageResource(savedInstanceState.getInt("dice3"))
        diece4.setImageResource(savedInstanceState.getInt("dice4"))
        diece5.setImageResource(savedInstanceState.getInt("dice5"))
        playerscore.text = savedInstanceState.getString("playerScore")
        numplayer.text = savedInstanceState.getString("numPlayer")

        gamemsg.text = savedInstanceState.getString("gameMsg")
        select1.text = savedInstanceState.getString("select1")
        select2.text = savedInstanceState.getString("select2")
        select3.text = savedInstanceState.getString("select3")
        select4.text = savedInstanceState.getString("select4")
        select5.text = savedInstanceState.getString("select5")
    }



    private fun diceRoller() {
        binding.gameMsg.text = "Dice Roller"

        val intent = intent

        // Retrieve the extra value with the key "maxScore"
        val maxScore = intent.getIntExtra("maxScore", 0)
        //User's chance and score is less then target then user has the chance
        if (playerScore <= maxScore && humanTurn) {
            playerAttempt += 1
            humanPlay()
        }

        //If the chance is not users and computer score is less than target
        if (playerScore <= maxScore && !humanTurn) {
            computerAttempt += 1
            computerPlay()
        }

        //If computer or player beats or equals to the target score
        if (computerScore >= maxScore || playerScore >= maxScore) {
            //Validates if both the computer and user had same number of rounds
            if (computerAttempt == playerAttempt) {
                //If tie both computer and player has the same score
                if (computerScore == playerScore){
                    humanPlay()
                    computerPlay()
                }else {
                    //Winner is determined if either of the two has come to the target points with equal number of attempts
                    determineWinner()
                }
            //if either of the two has a attempt less then computer or user is given another chance till attempts are equal
            } else if (computerAttempt < playerAttempt) {
                computerAttempt += 1
                computerPlay()
            } else if (playerAttempt < computerAttempt) {
                playerAttempt += 1
                humanPlay()
            }
        }
    }

    private fun computerPlay()
    {
        binding.gameMsg.text = "Computer is playing"
        binding.throwBtn.isEnabled = false
        binding.scoreBtn.isEnabled = false

        var total = 0
        // Array to store data 5 dices of type Dice Result
        val diceOutcomes = arrayOfNulls<DiceResult>(5)
        // Initializes a 6 sided dice
        val dice = Dice(6)

        //Stimulates the rolling of all 5 dices using the function diceRoll()
        for (i in diceOutcomes.indices){
            diceOutcomes[i] = dice.diceRoll()
        }

        //Sets the images of the dices
        scope.launch {
            delay(5000)
            withContext(Dispatchers.Main) {
                diceOutcomes[0]?.let { binding.dice1.setImageResource(it.diceSide) }
                diceOutcomes[1]?.let { binding.dice2.setImageResource(it.diceSide) }
                diceOutcomes[2]?.let { binding.dice3.setImageResource(it.diceSide) }
                diceOutcomes[3]?.let { binding.dice4.setImageResource(it.diceSide) }
                diceOutcomes[4]?.let { binding.dice5.setImageResource(it.diceSide) }

                //Updates the computer score
                for (i in diceOutcomes.indices) {
                    total += (diceOutcomes[i]?.randNum ?: 0)
                }
                computerScore += total

                // Calculates the difference between the player and computer score
                val scoreDiff = playerScore - computerScore

                //Toast.makeText(applicationContext, "SCORE $total", Toast.LENGTH_SHORT).show()
                val randNum = (1..2).random()

                //If the computer score is close to player score then throw again or computer chooses throw randomly
                if (scoreDiff in 1..10 || randNum == 1){
                    Toast.makeText(applicationContext,"Computer chose to throw",Toast.LENGTH_SHORT).show()
                    //binding.gameMsg.text = "Computer chose to throw"
                    computerSelectDice(diceOutcomes, 0)
                }else{
                    Toast.makeText(applicationContext,"Computer chose to score",Toast.LENGTH_SHORT).show()
                    var temp = playerScore.toString()
                    var temp1 = computerScore.toString()
                    binding.playerScore.text = "H:$temp/C:$temp1"
                    //Computer ends turn and user is given the chance
                    humanTurn = true
                    diceRoller()
                }
            }
        }
    }

    private fun humanPlay()
    {
        Toast.makeText(applicationContext,"Human Play",Toast.LENGTH_SHORT).show()
        //Array of type DiceResult in order to store data per dice
        val diceOutcomes = arrayOfNulls<DiceResult>(5)
       //Initializes a 6 sided dice
        val dice = Dice(6)

        //Stimulates the rolling of the dice with the diceRoll() function
        scope.launch {
            delay(2000)
            withContext(Dispatchers.Main){
                for (i in diceOutcomes.indices){
                    diceOutcomes[i] = dice.diceRoll()
                }
                //Sets the images for the dice
                diceOutcomes[0]?.let { binding.dice1.setImageResource(it.diceSide) }
                diceOutcomes[1]?.let { binding.dice2.setImageResource(it.diceSide) }
                diceOutcomes[2]?.let { binding.dice3.setImageResource(it.diceSide) }
                diceOutcomes[3]?.let { binding.dice4.setImageResource(it.diceSide) }
                diceOutcomes[4]?.let { binding.dice5.setImageResource(it.diceSide) }

                var total = 0

                for (i in diceOutcomes.indices) {
                    total += (diceOutcomes[i]?.randNum ?: 0)
                }
                //Updates player total
                playerScore += total
                binding.throwBtn.isEnabled = true
                binding.scoreBtn.isEnabled = true
            }
        }

        binding.gameMsg.text = "Tap Throw to re-roll"
        binding.dice1.isEnabled = false
        binding.dice2.isEnabled = false
        binding.dice3.isEnabled = false
        binding.dice4.isEnabled = false
        binding.dice5.isEnabled = false

        //Player score is updated
        binding.scoreBtn.setOnClickListener {
            var temp = playerScore.toString()
            var temp1 = computerScore.toString()
            binding.playerScore.text = "H:$temp/C:$temp1"
            //User's turn ends and computer is given the chance
            humanTurn = false
            diceRoller()
        }

        //Player can choose to re-roll up to two times
        binding.throwBtn.setOnClickListener {
            selectDice(diceOutcomes, 0)
        }
    }

    private fun computerSelectDice(diceOutcomes: Array<DiceResult?>, numRolls: Int)
    {
        var rollNum = numRolls
        var randNum = 0
        var numDices = 0
        var total = 0
        rollNum += 1

        numDices = (1..5).random()

        // Calculates the difference between the player and computer score
        val scoreDiff = playerScore - computerScore
        val intent = intent

        // Retrieve the extra value with the key "maxScore"
        val maxScore = intent.getIntExtra("maxScore", 0)
        //If the computer score is equal to the max score then end turn
        if (computerScore >= maxScore){
            var temp = playerScore.toString()
            var temp1 = computerScore.toString()
            //Computer ends turn and user is given the chance
            binding.playerScore.text = "H:$temp/C:$temp1"
            humanTurn = true
            diceRoller()
        }

        //Allows only 2 re-rolls
        if (rollNum < 3) {

            //If the computer has re-rolled but still the difference is more than 20 then re roll
            if (rollNum >= 1 && scoreDiff > 20) {
                binding.gameMsg.text = "Computer chose to re-roll"
                for (x in 1 until numDices + 1) {
                    //Computer randomly selects dices to keep
                    randNum = (1..5).random()
                    when (randNum) {
                        1 -> {
                            diceOutcomes[0]?.isSelected = true
                            binding.gameMsg.text = "Dice 1 selected"
                        }
                        2 -> {
                            diceOutcomes[1]?.isSelected = true
                            binding.gameMsg.text = "Dice 2 selected"
                        }
                        3 -> {
                            diceOutcomes[2]?.isSelected = true
                            binding.gameMsg.text = "Dice 3 selected"
                        }
                        4 -> {
                            diceOutcomes[3]?.isSelected = true
                            binding.gameMsg.text = "Dice 4 selected"
                        }
                        5 -> {
                            diceOutcomes[4]?.isSelected = true
                            binding.gameMsg.text = "Dice 5 selected"
                        }
                    }
                }

                //After dices are selected dice is rolled
                total = rollingDice(diceOutcomes)
                //Score is updated
                computerScore += total
                binding.gameMsg.text = "Selecting a dice computer"
                Toast.makeText(applicationContext,"Repeat function",Toast.LENGTH_SHORT).show()
                //Recursively calls the function till a total of 3 rolls are done
                computerSelectDice(diceOutcomes, rollNum)
            }
            else {
                Toast.makeText(applicationContext,"Computer chose to get score",Toast.LENGTH_SHORT).show()
                var temp = playerScore.toString()
                var temp1 = computerScore.toString()
                binding.playerScore.text = "H:$temp/C:$temp1"
                //Computer ends turn and user is given the chance
                humanTurn = true
                diceRoller()
            }
        }else{
            Toast.makeText(applicationContext,"Computer chose to get score",Toast.LENGTH_SHORT).show()
            var temp = playerScore.toString()
            var temp1 = computerScore.toString()
            //Computer ends turn and user is given the chance
            binding.playerScore.text = "H:$temp/C:$temp1"
            humanTurn = true
            diceRoller()
        }
    }

    private fun selectDice(diceOutcomes: Array<DiceResult?>, numRolls: Int)
    {
        var rollNum = numRolls
        var total = 0
        rollNum += 1
        binding.scoreBtn.isEnabled = false
        binding.dice1.isEnabled = true
        binding.dice2.isEnabled = true
        binding.dice3.isEnabled = true
        binding.dice4.isEnabled = true
        binding.dice5.isEnabled = true

        binding.gameMsg.text = "Select dices to keep"

        //Validated if the user has done a total of 3 rolls
        if (rollNum == 3){
            binding.gameMsg.text = "Human turn ended. Score Updated"
            var temp = playerScore.toString()
            var temp1 = computerScore.toString()
            binding.playerScore.text = "H:$temp/C:$temp1"
            //If so users turn ends and computer is given the chance
            humanTurn = false
            diceRoller()
        }


        if (rollNum == 2 || rollNum < 3)
        {
            binding.scoreBtn.isEnabled = rollNum == 2 //If rollNum = 2 then score button is enabled

            if (rollNum < 1){
                binding.gameMsg.text = "Select Dices to keep else Tap score to end TURN!"
            }

            binding.scoreBtn.setOnClickListener {
                binding.gameMsg.text = "Score updated"
                var temp = playerScore.toString()
                var temp1 = computerScore.toString()
                binding.playerScore.text = "H:$temp/C:$temp1"
                // Users turn ends and computer is given the chance
                humanTurn = false
                diceRoller()
            }

            //User is able to re-roll by selecting the dices to keep
            //After selecting once the throw button is tapped dice rolling is stimulated
            binding.dice1.setOnClickListener {
                diceOutcomes[0]?.isSelected = true
                binding.gameMsg.text = "Dice 1 selected"
                binding.select1.visibility = View.VISIBLE
                binding.throwBtn.setOnClickListener {
                    total = rollingDice(diceOutcomes)
                    playerScore += total
                    selectDice(diceOutcomes, rollNum)
                }
            }
            binding.dice2.setOnClickListener {
                diceOutcomes[1]?.isSelected = true
                binding.select2.visibility = View.VISIBLE
                binding.gameMsg.text = "Dice 2 selected"
                binding.throwBtn.setOnClickListener {
                    total = rollingDice(diceOutcomes)
                    playerScore += total
                    selectDice(diceOutcomes, rollNum)
                }
            }
            binding.dice3.setOnClickListener {
                diceOutcomes[2]?.isSelected = true
                binding.select3.visibility = View.VISIBLE
                binding.gameMsg.text = "Dice 3 selected"
                binding.throwBtn.setOnClickListener {
                    total = rollingDice(diceOutcomes)
                    playerScore += total
                    selectDice(diceOutcomes, rollNum)
                }
            }
            binding.dice4.setOnClickListener {
                diceOutcomes[3]?.isSelected = true
                binding.select4.visibility = View.VISIBLE
                binding.gameMsg.text = "Dice 4 selected"
                binding.throwBtn.setOnClickListener {
                    total = rollingDice(diceOutcomes)
                    playerScore += total
                    selectDice(diceOutcomes, rollNum)
                }
            }
            binding.dice5.setOnClickListener {
                diceOutcomes[4]?.isSelected = true
                binding.select5.visibility = View.VISIBLE
                binding.gameMsg.text = "Dice 5 selected"
                binding.throwBtn.setOnClickListener {
                    rollingDice(diceOutcomes)
                    selectDice(diceOutcomes, rollNum)
                }
            }
        }
    }

    private fun rollingDice(diceOutcomes: Array<DiceResult?>) : Int
    {
        Toast.makeText(applicationContext,"Rolling dice",Toast.LENGTH_SHORT).show()
        var total = 0

        binding.select1.visibility = View.GONE
        binding.select2.visibility = View.GONE
        binding.select3.visibility = View.GONE
        binding.select4.visibility = View.GONE
        binding.select5.visibility = View.GONE

        val dice = Dice(6)
        for (x in diceOutcomes.indices){
            Log.d(diceOutcomes[x].toString(),"Before")
            //Checks and rolls only the dices that are not selected
            if (diceOutcomes[x] != null && !diceOutcomes[x]?.isSelected!!){
                diceOutcomes[x] = dice.diceRoll()
            }
            //Unselects all the dices
            diceOutcomes[x]?.isSelected = false
            //Total of the roll is updated
            total += (diceOutcomes[x]?.randNum ?: 0)
            Log.d(diceOutcomes[x].toString(),"After")
        }
        //Sets the dice images
        scope.launch {
            delay(1000)
           withContext(Dispatchers.Main) {
                diceOutcomes[0]?.let { binding.dice1.setImageResource(it.diceSide) }
                diceOutcomes[1]?.let { binding.dice2.setImageResource(it.diceSide) }
                diceOutcomes[2]?.let { binding.dice3.setImageResource(it.diceSide) }
                diceOutcomes[3]?.let { binding.dice4.setImageResource(it.diceSide) }
                diceOutcomes[4]?.let { binding.dice5.setImageResource(it.diceSide) }
            }
        }
        //Total points of the roll is returned
        return total
    }

    private fun determineWinner()
    {
        val intent = Intent(this, Summary::class.java).apply {
            // Add the playerScore and computerScore as extras
            putExtra("playerScore", playerScore)
            putExtra("computerScore", computerScore)
        }
        startActivity(intent)


    }



}

//Custom data class to store data for Dice
data class DiceResult(val randNum: Int, val diceSide: Int, var isSelected: Boolean)

//Dice class
class Dice (private val numSides : Int){
    //Creates a 6 sided dice object
    fun diceRoll():DiceResult{
        val randNum = (1..6).random()
        val diceSide = when(randNum) {
            1 ->
            {
                R.drawable.dice1
            }
            2 ->
            {
                R.drawable.dice2
            }
            3 ->
            {
                R.drawable.dice3
            }
            4 ->
            {
                R.drawable.dice4
            }
            5 -> {
                R.drawable.dice5
            }
            else ->
            {
                R.drawable.dice6
            }
        }
        return DiceResult(randNum, diceSide, false)
    }
}