package com.example.diceexploder

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.exp


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val inputEditText = findViewById<EditText>(R.id.inputEditText);
        val resultTextView = findViewById<TextView>(R.id.resultTextView);
        val diceResultTextView = findViewById<TextView>(R.id.diceResultTextView);

        fab.setOnClickListener { view ->
            var results = rollDice(inputEditText.text.toString())
            println("results " + results)
            if (results.size > 0) {
                val result = results.get(results.lastIndex).get(0)
                val dice = results.dropLast(1)
                resultTextView.text = result.toString()
                diceResultTextView.text = dice.toString()
            } else {
                resultTextView.text = "Please input a valid roll"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun roll(dieCount: Int, numDie: Int): MutableList<Int> {
        var results = mutableListOf<Int>()
        println(numDie)
        for (i in 1..numDie) {
            val curResult = (1..numDie).random()
            results.add(curResult)
        }
        println("roll d" + dieCount + " " + results)
        return results
    }

    fun explode(dieCount: Int, numDie: Int): MutableList<Int> {
        var results = mutableListOf<Int>()
        for (i in 1..numDie) {
            var roll = (1..dieCount).random()
            results.add(roll)
            if (roll == dieCount) {
                results.plusAssign(explode(dieCount, 1))
            }
        }
        println("explode d" + dieCount + " " + results)
        return results
    }


    fun advantage(dieCount: Int, numDie: Int, advantageCount: Int, explode: Boolean): MutableList<Int> {
        var result = 0
        var results = mutableListOf<Int>()
        for (i in (1..advantageCount + numDie)) {
            if (explode) {
                results.plusAssign(explode(dieCount, 1))
            } else {
                results.plusAssign(roll(dieCount, 1))
            }
        }
        results.sort()
        results.reverse()
        println(results)
        return results
    }

    fun rollDice(rollInput: String): MutableList<MutableList<Int>> {
        var result = 0
        var results = mutableListOf<MutableList<Int>>()
        if (rollInput == "") {
            return results
        }
        val rollInputClean = rollInput.replace("\\s".toRegex(), "")
        val rolls = rollInputClean.split("+")
        for (curRoll in rolls) {
            var rollSplit = curRoll.split("d")
            rollSplit = rollSplit.toMutableList()
            println(rollSplit)
            val numDie = rollSplit[0].toInt()
            var explodeOn = false

            if (rollSplit[1].contains("!")) {
                explodeOn = true
                rollSplit[1] = rollSplit[1].replace("!", "")
            }
            val dieCount = rollSplit[1].toInt()
            if (explodeOn) {
                results.add(explode(dieCount, numDie))
            } else {
                results.add(roll(dieCount, numDie))
            }
            println(results)
        }
        var total = 0
        for (result in results) {
            for (res in result)  {
                total += res
            }
        }
        results.add(mutableListOf<Int>(total))
        return results
    }

}
