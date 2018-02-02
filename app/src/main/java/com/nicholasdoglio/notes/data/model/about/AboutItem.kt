package com.nicholasdoglio.notes.data.model.about

/**
 * @author Nicholas Doglio
 * @param imageId drawable resource for about icon
 * @param text text description of each item
 * @param link website URL for better understanding each of item
 */
data class AboutItem(val imageId: Int, val text: String, val link: String)