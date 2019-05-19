package com.nicholasdoglio.notes.ui.tile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.nicholasdoglio.notes.ui.MainActivity

private const val CREATE_NOTE_DEEP_LINK_URL = "com.nicholasdoglio.notes://create_new_note"

@RequiresApi(Build.VERSION_CODES.N)
class CreateNoteTile : TileService() {

    override fun onClick() {
        super.onClick()
        startActivityAndCollapse(Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(CREATE_NOTE_DEEP_LINK_URL)
        }
        )
    }
}
