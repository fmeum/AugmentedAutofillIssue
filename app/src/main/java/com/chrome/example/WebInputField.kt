package com.chrome.example

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.core.view.forEach

// EditText with manual Autofill disabled; mimics Chrome's custom views for <input> elements
@SuppressLint("AppCompatCustomView")
class WebInputField(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    init {
        customInsertionActionModeCallback = object : android.view.ActionMode.Callback {
            override fun onCreateActionMode(mode: android.view.ActionMode, menu: Menu) = true

            override fun onPrepareActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
                // Disable the manual Autofill context menu item
                var autofillMenuItemId = 0
                menu.forEach {
                    if (it.title == "Autofill") {
                        autofillMenuItemId = it.itemId
                    }
                }
                menu.removeItem(autofillMenuItemId)
                return true
            }

            override fun onActionItemClicked(mode: android.view.ActionMode, item: MenuItem) = true

            override fun onDestroyActionMode(mode: android.view.ActionMode) {}
        }
    }
}