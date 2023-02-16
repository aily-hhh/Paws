package androidx.appcompat.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View

@SuppressLint("RestrictedApi")
class CustomPopUpMenu constructor(context: Context, view: View): PopupMenu(context, view) {

    init {
        mPopup.setForceShowIcon(true)
    }
}