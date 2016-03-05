from ClarifaiParse import ClarifaiParse
import pywikibot


def run():
    app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
    app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
<<<<<<< HEAD:wikiOld.py
    c = ClarifaiParse(app_id,app_secret)
    count = 0
    stop_count = 3
=======
    c = ClarifaiParse(app_id, app_secret)
    
    site = pywikibot.Site()


>>>>>>> dea2cbfed4ff1b083358790115e2eb5d0326ecb4:wiki.py
    for result in c.parse("earthworm.jpg"):
        name = result["tag"]
        try:
<<<<<<< HEAD:wikiOld.py
            page = wikipedia.WikipediaPage(result["tag"])
            html = page.html()
            print(result["tag"], "infobox biota" in html)
            if ("infobox biota" in html):
                count += 1
            if (count == stop_count):
                break
        except wikipedia.exceptions.PageError:
            print("page not found for", result["tag"])
=======
            page = pywikibot.Page(site, name)
            s = page.title
            t = page.get()
            print(result, "{{Taxobox" in t or "{{speciesbox" in t)
        except pywikibot.exceptions.NoPage:
            print(name, "page does not exist")
>>>>>>> dea2cbfed4ff1b083358790115e2eb5d0326ecb4:wiki.py
            continue
        except pywikibot.exceptions.IsRedirectPage:
            print(name, "is a redirect page")
            continue



run()
