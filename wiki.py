from ClarifaiParse import ClarifaiParse
import json
import wikipedia
from bs4 import BeautifulSoup
import httplib2

class WikiParse:
    def __init__(self):
        app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
        app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
        self.c = ClarifaiParse(app_id, app_secret)
    
    def is_animal(self, url, http):
        status, response = http.request(url)
        soup = BeautifulSoup(response, "lxml")
        table = soup.find("table", {"class":"infobox biota"})
        
        return table != None

    def get_matches(self, name, max_count=3):
        matches = []
        http = httplib2.Http()

        i = 0
        for result in self.c.parse(name):
            if i >= max_count: break
            name = result["tag"]
            
            try:
                wiki_response = wikipedia.WikipediaPage(name)
                if self.is_animal(wiki_response.url, http):
                    print(name, "is a real and relevant article")
                    result["summary"] = wiki_response.summary
                    matches.append(result)
                    i += 1
                    
            except wikipedia.exceptions.DisambiguationError as e:
                continue
            except wikipedia.exceptions.PageError as e:
                print(name, "is not a real article")
                continue
            
        return json.dumps(matches)


if __name__ == "__main__":
    w = WikiParse()
    url = "http://kids.nationalgeographic.com/content/dam/kids/photos/animals/Mammals/A-G/gray-wolf-snow.jpg.adapt.945.1.jpg"
    matches = w.get_matches(url)

