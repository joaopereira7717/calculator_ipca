package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {

        val btnToUse = getBtnToUse(btn)

        _equationText.value?.let {
            if (btnToUse == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }

            if (btnToUse == "C") {
                if (it.isNotEmpty()) {
                    _equationText.value = it.substring(0, it.length - 1)
                }
                return
            }

            if (btnToUse == "=") {
                _equationText.value = _resultText.value
                return
            }

            _equationText.value = it + btnToUse

            try {
                _resultText.value = calculateResult(_equationText.value.toString())
            } catch (error: Exception) {
                Log.e("Error calculating", error.toString())
            }
        }
    }

    private fun calculateResult(equation: String): String {
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()
        var finalResult =
            context.evaluateString(scriptable, equation, "Javascript", 1, null).toString()
        if (finalResult.endsWith(".0")) {
            finalResult = finalResult.replace(".0", "")
        }
        return finalResult
    }

    private fun getBtnToUse(btn: String): String {
        return when (btn) {
            "x" -> "*"
            ":" -> "/"
            else -> btn
        }
    }


}
