from ClarifaiParse import ClarifaiParse
import pywikibot
import json

class WikiParse:
    def __init__(self):
        app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
        app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
        self.c = ClarifaiParse(app_id, app_secret)

    def get_matches(self, name="earthworm.jpg"):
        matches = []
        site = pywikibot.Site()
        
        for result in self.c.parse(name):
            name = result["tag"]
            try:
                page = pywikibot.Page(site, name)
                t = page.get()
                if "taxobox" in t.lower() or "speciesbox" in t.lower():
                    print(name, "is found")
                    result["wiki"] = t
                    matches.append(t)
            except pywikibot.exceptions.NoPage:
                print(name, "page does not exist")
                continue
            except pywikibot.exceptions.IsRedirectPage:
                print(name, "is a redirect page")
                continue
            
        return json.dumps(matches)


if __name__ == "__main__":
    w = WikiParse()
    url = "http://kids.nationalgeographic.com/content/dam/kids/photos/animals/Mammals/A-G/gray-wolf-snow.jpg.adapt.945.1.jpg"
    matches = w.get_matches(url)

