package com.example.calculator

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonClicks()
    }

    private fun buttonClicks() {
        binding.apply {
            btClear.setOnClickListener {
                clear()
            }
            btOne.setOnClickListener {
                type(it)
            }
            btTwo.setOnClickListener {
                type(it)
            }
            btThree.setOnClickListener {
                type(it)
            }
            btFour.setOnClickListener {
                type(it)
            }
            btFive.setOnClickListener {
                type(it)
            }
            btSix.setOnClickListener {
                type(it)
            }
            btSeven.setOnClickListener {
                type(it)
            }
            btEight.setOnClickListener {
                type(it)
            }
            btNine.setOnClickListener {
                type(it)
            }
            btZero.setOnClickListener {
                type(it)
            }
            btPlus.setOnClickListener {
                arithmeticOperation(it)
            }
            btMinus.setOnClickListener {
                arithmeticOperation(it)
            }
            btMultiply.setOnClickListener {
                arithmeticOperation(it)
            }
            btDivide.setOnClickListener {
                arithmeticOperation(it)
            }
            btPercent.setOnClickListener {
                percentage(it)
            }
            btParentheses.setOnClickListener {
                parentheses(it)
            }
            btEqual.setOnClickListener {
                calculation()
            }
            btDot.setOnClickListener {
                dotClick(it)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun calculation() {
        binding.apply {
            val txt: String = operand + currentInput
            val expression: Expression = ExpressionBuilder(txt).build()
            try {
                val result: Double = expression.evaluate()
                // Format the result to 5 decimal places
                val formattedResult = String.format("%.5f", result).trimEnd('0').trimEnd('.')
                etDisplay.text = getBoldString(formattedResult)
                operand = ""
                currentInput = formattedResult
            } catch (arithmeticException: ArithmeticException) {
                etDisplay.text = arithmeticException.message
            } catch (illegalArgumentException: IllegalArgumentException) {
                etDisplay.text = illegalArgumentException.message
            }
        }
    }


    private fun percentage(view: View?) {
        val button = view as MaterialButton
        binding.apply {
            val currentText = etDisplay.text.toString()
            if (currentText.isNotEmpty() && !currentText.lastSameSymbol()) {
                if (button.text.toString() == "%") {
                    if (currentText.last().isDigit()) {
                        val number = currentText.toDouble()
                        val percentage = number / 100
                        etDisplay.text = percentage.toString()
                    }
                }
            }
        }
    }

    private fun parentheses(it: View?) {
        val text = binding.etDisplay.text.toString()

        if (text.isNotEmpty()) {
            try {
                val number = text.toDouble()
                val toggledNumber = -number
                binding.etDisplay.text = toggledNumber.toString()
            } catch (e: NumberFormatException) {

            }
        }
    }

    private fun dotClick(view: View?) {
        val button = view as MaterialButton
        binding.apply {
            if (etDisplay.text.isNotEmpty() && !etDisplay.text.toString().lastSameSymbol()) {
                if (!button.text.toString().compareLast(etDisplay.text.toString())) {
                    type(button)
                }
            }
        }
    }

    private fun arithmeticOperation(it: View?) {
        val button = it as MaterialButton
        binding.apply {
            if (etDisplay.text.isNotEmpty() && etDisplay.text.toString().lastSameSymbol()) {
                if (!button.text.toString().compareLast(binding.etDisplay.text.toString())) {
                    etDisplay.text = etDisplay.text.toString().dropLast(1)
                    type(button)
                }
            } else {
                type(button)
            }
        }
    }

    private var currentInput = ""
    private var operator: Char? = null
    private var operand: String = ""
    private fun type(view: View) {
        val button = view as MaterialButton
        val inputText = button.text.toString()

        if (inputText.toDoubleOrNull() != null) {
            currentInput += inputText
            binding.etDisplay.text = getBoldString(currentInput)
        } else {
            when (inputText) {
                "+", "-", "*", "/" -> {
                    if (currentInput.isNotEmpty()) {
                        operand += currentInput + inputText
                        currentInput = ""
                    }
                }
            }
        }
    }

    private fun clear() {
        currentInput = ""
        operator = null
        operand = ""
        binding.etDisplay.text = getBoldString("0")
    }

    private fun getBoldString(input: String): SpannableString {
        val spannableString = SpannableString(input)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            input.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

}

private fun String.lastSameSymbol(): Boolean= (this.last().toString().isArithmeticSymbol())

private fun String.isArithmeticSymbol(): Boolean= (this == "+" || this == "-" || this == "*" || this == "/")

fun String.compareLast(currentText: String): Boolean = (this.last() == currentText.last())
