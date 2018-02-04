package com.nicholasdoglio.notes.util

/**
 * @author Nicholas Doglio
 */

//TODO move all consts here and all user facing Strings to strings.xml
object Const {
    /** Note Fragment arguments ID for passing arguments to a NoteFragment  */
    const val noteFragmentArgumentId = "NOTE_ID"

    /** Shortcut ID to quickly open the NoteFragment from the homescreen */
    const val shortcutNoteIntentId = "createNote"

    /** Transaction ID for each fragment used in the NavigationController */
    const val noteListFragmentId = "NOTE_LIST"
    const val noteFragmentId = "NOTE"
    const val aboutFragmentId = "ABOUT"
    const val libsFragmentId = "LIBS"

    /** savedInstanceState keys to restore  data in the NoteFragment during recreation */
    const val noteFragmentTitleKey = "TITLE"
    const val noteFragmentContentsKey = "CONTENTS"
}