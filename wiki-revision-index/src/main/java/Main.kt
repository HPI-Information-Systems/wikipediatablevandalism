@file:JvmName("Main")

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.esotericsoftware.kryo.Kryo
import db.RevisionRepository
import db.SqliteDatabase
import parser.RevisionParser
import java.io.File

fun main(args: Array<String>) {
    val arguments = Arguments()
    val cmd = JCommander.newBuilder()
            .addObject(arguments)
            .build()

    try {
        cmd.parse(*args)
        SqliteDatabase.open(arguments.indexPath)

        val repository = RevisionRepository(SqliteDatabase.connection)
        Indexer(repository)
                .parseRecursively(arguments.dataPath)
                .blockingSubscribe()

        SqliteDatabase.close()
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
