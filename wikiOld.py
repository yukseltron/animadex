import wikipedia
from ClarifaiParse import ClarifaiParse
from bs4 import BeautifulSoup

def run():
    app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
    app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
    c = ClarifaiParse(app_id,app_secret)
    count = 0
    stop_count = 3
    for result in c.parse("earthworm.jpg"):
     
        try:
            page = wikipedia.WikipediaPage(result["tag"])
            html = page.html()
            print(result["tag"], "infobox biota" in html)
            if ("infobox biota" in html):
                count += 1
            if (count == stop_count):
                break
        except wikipedia.exceptions.PageError:
            print("page not found for", result["tag"])
            continue
        except Exception as e:
            continue

run()
