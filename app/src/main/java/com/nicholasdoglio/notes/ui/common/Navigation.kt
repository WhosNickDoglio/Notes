package com.nicholasdoglio.notes.ui.common

/**
 * @author Nicholas Doglio
 */
interface Navigation {

    fun openList()

    fun openNote(id: Long = 0)

    fun openAbout()

    fun openLibs()
}