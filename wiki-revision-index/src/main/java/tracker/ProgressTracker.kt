package tracker

import me.tongfei.progressbar.ProgressBar

class ProgressTracker(totalPages: Long) {
    private val progressBar = ProgressBar("Parsing Pages", totalPages).also {
        it.start()
        it.step()
    }

    fun trackPageParsed() {
        progressBar.step()
    }
}
