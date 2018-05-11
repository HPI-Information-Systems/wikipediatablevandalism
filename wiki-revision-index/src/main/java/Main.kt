@file:JvmName("Main")

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import kotlinx.coroutines.experimental.runBlocking
import tracker.ProgressTracker
import utils.countWikiPages
import java.io.File

fun main(args: Array<String>) = runBlocking {
    val arguments = Arguments()
    val cmd = JCommander.newBuilder()
            .addObject(arguments)
            .build()

    try {
        cmd.parse(*args)
        val file = arguments.dataPath
        val totalPages = file.countWikiPages()

        Indexer(ProgressTracker(totalPages.toLong()))
                .parseRecursively(file)
                .forEach { it.join() }
    } catch (e: ParameterException) {
        cmd.usage()
    }
}

class Arguments {
    @Parameter(names = ["--data"], description = "Path of directory containing *.parsed files", required = true)
    lateinit var dataPath: File

    @Parameter(names = ["--index"], description = "Path of directory to index")
    var indexPath = File("./")

    @Parameter(names = ["--help"], help = true)
    var help: Boolean = false
}
