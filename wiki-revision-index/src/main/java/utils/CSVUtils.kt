package utils

import model.AnnotatedRevision
import model.PageRevision
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter

fun CSVParser.readPageRevisions() = this.records
        .map { PageRevision(it.get("page_id").toBigInteger(), it.get("revision_id").toBigInteger()) }
        .toList()

fun CSVPrinter.printAnnotatedRevision(annotatedRevision: AnnotatedRevision) {
    this.printRecord(annotatedRevision.id,
            annotatedRevision.revertId,
            annotatedRevision.hasComment.toInt(),
            annotatedRevision.isVandalism.toInt(),
            annotatedRevision.isReverted.toInt(),
            annotatedRevision.isClueBot.toInt()
    )
}

fun Boolean.toInt(): Int = if (this) 1 else 0
