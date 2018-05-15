@file:JvmName("Main")

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import db.DatabaseProvider
import kotlinx.coroutines.experimental.runBlocking
import tracker.ProgressTracker
import utils.wikiPages
import java.io.File

fun main(args: Array<String>) = runBlocking {
    val arguments = Arguments()
    val cmd = JCommander.newBuilder()
            .addObject(arguments)
            .build()

    var progressTracker: ProgressTracker? = null

    try {
        cmd.parse(*args)

        val databaseProvider = DatabaseProvider

        val file = arguments.dataPath
        val pages = file.wikiPages()
        progressTracker = ProgressTracker(pages.size.toLong())
        Indexer(databaseProvider, progressTracker)
                .parse(pages)
                .forEach { it.await() }
    } catch (e: ParameterException) {
        cmd.usage()
    } finally {
        if (progressTracker != null) {
            progressTracker.stop()
        }
    }
}

class Arguments {
    @Parameter(names = ["--data"], description = "Path of directory containing *.parsed files", required = true)
    lateinit var dataPath: File

    @Parameter(names = ["--help"], help = true)
    var help: Boolean = false
}
