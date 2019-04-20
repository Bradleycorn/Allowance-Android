package net.bradball.allowance.ui

import android.app.Activity

interface ToolbarContainer {
    var toolbarTitle: String?
}

val Activity.asToolbarContainer: ToolbarContainer?
    get() = (this as? ToolbarContainer)