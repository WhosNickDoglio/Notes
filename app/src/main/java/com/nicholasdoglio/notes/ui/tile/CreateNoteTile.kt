package com.nicholasdoglio.notes.ui.tile

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import com.nicholasdoglio.notes.ui.MainActivity
import com.nicholasdoglio.notes.util.Const

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
class CreateNoteTile : TileService() {

    //TODO clean this up
    override fun onClick() {
        super.onClick()

        val intent = Intent(applicationContext, MainActivity::class.java)

        intent.putExtra(Const.shortcutNoteIntentId, Const.shortcutNoteIntentId)

        startActivityAndCollapse(intent)
    }
}