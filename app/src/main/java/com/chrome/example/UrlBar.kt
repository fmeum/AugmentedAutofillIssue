package com.chrome.example

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.ViewStructure
import android.widget.EditText


@SuppressLint("AppCompatCustomView")
class UrlBar(context: Context, attrs: AttributeSet?) : EditText(context, attrs) {

    private var providingAutofillStructure: Boolean = false

    override fun onProvideAutofillStructure(structure: ViewStructure?, flags: Int) {
        providingAutofillStructure = true
        super.onProvideAutofillStructure(structure, flags)
        providingAutofillStructure = false
    }

    override fun getText(): Editable? {
        return if (providingAutofillStructure) SpannableStringBuilder("https://google.com") else super.getText()
    }
}