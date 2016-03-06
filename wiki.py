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
        self.rank = ["domain", "kingdom", "phylum", 
                    "class", "subclass", "superorder",
                    "order", "suborder", "family",
                    "subfamily", "genus", "species"]
    
    def linnean_values(self, table):
        t = str(table).lower()
        for i in range(len(self.rank)-1, -1, -1):
            if self.rank[i] in t:
                print(self.rank[i])
                return i

        return -1

    def linnean_table(self, url, http):
        status, response = http.request(url)
        soup = BeautifulSoup(response, "lxml")
        table = soup.find("table", {"class":"infobox biota"})
        return table

    def get_matches(self, name, max_count=3):
        matches = []
        http = httplib2.Http()

        i = 0
        for result in self.c.parse(name):
            if i >= max_count: break
            name = result["tag"]
            
            try:
                wiki_response = wikipedia.WikipediaPage(name)
                table = self.linnean_table(wiki_response.url, http)
                if table != None: 
                    linnean = self.linnean_values(table)
                    result["linnean"] = linnean
                    result["summary"] = wiki_response.summary
                   
                    print(name, linnean, "is a real and relevant article")
                    matches.append(result)
                    i += 1
                    
            except wikipedia.exceptions.DisambiguationError as e:
                continue
            except wikipedia.exceptions.PageError as e:
                print(name, "is not a real article")
                continue
         
        matches = sorted(matches, key=lambda k: -k["linnean"])
        return json.dumps(matches)


if __name__ == "__main__":
    w = WikiParse()
    url = "http://pngimg.com/upload/butterfly_PNG1037.png"
    matches = w.get_matches(url)

