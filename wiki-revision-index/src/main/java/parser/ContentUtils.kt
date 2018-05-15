package parser

import org.apache.commons.collections.CollectionUtils


fun changedTables(previousTables: List<String>, tables: List<String>): Int {
    return CollectionUtils.disjunction(tables, previousTables).size
}
