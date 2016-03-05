from ClarifaiParse import ClarifaiParse
import pywikibot


def run():
    app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
    app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
    c = ClarifaiParse(app_id, app_secret)
    
    site = pywikibot.Site()


    for result in c.parse("earthworm.jpg"):
        name = result["tag"]
        try:
            page = pywikibot.Page(site, name)
            s = page.title
            t = page.get()
            print(result, "{{Taxobox" in t or "{{speciesbox" in t)
        except pywikibot.exceptions.NoPage:
            print(name, "page does not exist")
            continue
        except pywikibot.exceptions.IsRedirectPage:
            print(name, "is a redirect page")
            continue



run()
