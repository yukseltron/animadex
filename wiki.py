import wikipedia
from ClarifaiParse import ClarifaiParse
from bs4 import BeautifulSoup

def run():
    c = ClarifaiParse()
    for result in c.parse("earthworm.jpg"):
     
        try:
            page = wikipedia.WikipediaPage(result["tag"])
            html = page.html()
            print(result["tag"], "infobox biota" in html)

        except wikipedia.exceptions.PageError:
            print("page not found for", result["tag"])
            continue
        except Exception as e:
            continue

run()
