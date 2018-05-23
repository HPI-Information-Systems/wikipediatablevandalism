#! -*- encoding: utf-8 -*-


class OpenHandler(object):

    def __init__(self, open_method, shutdown_method=None):
        self.open_method = open_method
        self.shutdown_method = shutdown_method

    def __enter__(self):
        return self

    def open(self, revision_id):
        url = "https://en.wikipedia.org/w/index.php?diff={}".format(revision_id)
        self.open_method(url)

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.shutdown_method:
            self.shutdown_method()

    @classmethod
    def selenium(cls):
        from selenium import webdriver
        driver = webdriver.Chrome()
        return cls(driver.get, driver.close)

    @classmethod
    def fallback(cls):
        import webbrowser
        return cls(webbrowser.open)
