@file:JvmName("Main")

package commentfilter

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import java.io.File


fun main(args: Array<String>) {
    val arguments = Arguments()
    val cmd = JCommander.newBuilder()
            .addObject(arguments)
            .build()

    try {
        cmd.parse(*args)
        VandalismRevisionFinder().parse(arguments.dataPath, arguments.revisionPath, arguments.outputPath)
    } catch (e: ParameterException) {
        cmd.usage()
    }
}

class Arguments {
    @Parameter(names = ["--revisions"], description = "Path to csv file containing page ids and revisions", required = true)
    lateinit var revisionPath: File

    @Parameter(names = ["--data"], description = "Path of directory containing *.parsed files", required = true)
    lateinit var dataPath: File

    @Parameter(names = ["--output"], description = "Path to output csv file", required = true)
    lateinit var outputPath: File

    @Parameter(names = ["--help"], help = true)
    var help: Boolean = false
}
