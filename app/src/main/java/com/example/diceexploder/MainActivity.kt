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

        fab.setOnClickListener { view ->
            resultTextView.text = rollDice(inputEditText.text.toString()).toString()
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

    fun roll(dieCount: Int, numDie: Int): Int {
        var result = 0
        for (i in 1..numDie) {
            result += (1..numDie).random()
        }
        println("roll d" + dieCount + " " + result)
        return result
    }

    fun explode(dieCount: Int, numDie: Int): Int {
        var result = 0
        for (i in 1..numDie) {
            var roll = (1..dieCount).random()
            result += roll
            if (roll == dieCount) {
                result += explode(dieCount, 1)
            }
        }
        println("explode d" + dieCount + " " + result)
        return result
    }


    fun advantage(dieCount: Int, numDie: Int, advantageCount: Int, explode: Boolean): Int {
        var result = 0
        var results = mutableListOf<Int>()
        for (i in (1..advantageCount + numDie)) {
            if (explode) {
                results.add(explode(dieCount, 1))
            } else {
                results.add(roll(dieCount, 1))
            }
        }
        results.sort()
        results.reverse()
        println(results)
        for (i in 1..numDie) {
            result += results[i]
        }
        return result
    }

    fun rollDice(rollInput: String): Int {
        var result = 0
        var results = mutableListOf<Int>()
        val rollInputClean = rollInput.replace("\\s".toRegex(), "")
        val rolls = rollInputClean.split("+")
        for (curRoll in rolls) {
            var rollSplit = curRoll.split("d")
            val numDie = rollSplit[0].toInt()
            var explodeOn = false

            if (rollSplit[1].contains("!")) {
                explodeOn = true
            }
            val dieCount = rollSplit[1].replace("!", "").toInt()
            if (explodeOn) {
                results.add(explode(dieCount, numDie))
            } else {
                results.add(roll(dieCount, numDie))
            }
        }
        results.sort()
        for (res in results) {
            result += res;
        }
        return result
    }

}
