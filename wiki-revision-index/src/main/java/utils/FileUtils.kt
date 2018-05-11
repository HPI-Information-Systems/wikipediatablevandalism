package utils

import java.io.File

fun File.wikiPages(): Sequence<File> = walkTopDown().filter { it.isFile && it.name.endsWith(".parsed") }

fun File.countWikiPages() = wikiPages().count()
