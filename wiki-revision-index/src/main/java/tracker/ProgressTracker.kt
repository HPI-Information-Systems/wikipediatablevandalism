package tracker

import me.tongfei.progressbar.ProgressBar

class ProgressTracker(private val totalPages: Long) {
    private val progressBar = ProgressBar("Parsing Pages", totalPages).also {
        it.start()
    }

    fun trackPageParsed() {
        progressBar.step()

    }

    fun stop() {
        progressBar.stop()
    }
}
