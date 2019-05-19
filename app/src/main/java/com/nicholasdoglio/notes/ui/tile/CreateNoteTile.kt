package com.nicholasdoglio.notes.ui.tile

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.nicholasdoglio.notes.ui.MainActivity

const val NOTES_TILE_SHORTCUT = "NOTES_TILE_SHORTCUT"

@RequiresApi(Build.VERSION_CODES.N)
class CreateNoteTile : TileService() {

    override fun onClick() {
        super.onClick()
        startActivityAndCollapse(Intent(applicationContext, MainActivity::class.java)
            .apply {
                putExtra(NOTES_TILE_SHORTCUT, NOTES_TILE_SHORTCUT)
            }
        )
    }
}
