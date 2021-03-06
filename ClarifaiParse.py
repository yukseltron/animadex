from clarifai.client import ClarifaiApi

class ClarifaiParse:
    def __init__(self, app_id=None, app_secret=None):
        self.api = ClarifaiApi(app_id, app_secret)

    def parse(self, url):
        try:
            api_result = self.api.tag_image_urls(url)
            
            if api_result["status_code"] == "OK":
                matches = []

                for r in api_result['results']:
                    tag = r["result"]["tag"]
                    number_results = len(tag["classes"])
                    for i in range(number_results):
                        data = {"tag": tag["classes"][i], "probs": tag["probs"][i], "id": tag["concept_ids"][i]}
                        matches.append(data)

                return matches
            else:
                print(api_result["status_code"])
                print(api_result["status_msg"])
                return []
        except IOError:
            print("can't open", filename)
            return []
        except Exception as e:
            print(e)
            return []

if __name__ == "__main__":
    app_id = "aoaEzeM8d6fiL2L1eX--OtjXFAaPe_CDo6zFvEJD"
    app_secret = "fjhnJKSdHcZJACroOvCCoNf4tgi9YlOQpi52z-Pb"
    
    c = ClarifaiParse(app_id, app_secret)
    url = "http://kids.nationalgeographic.com/content/dam/kids/photos/animals/Mammals/A-G/gray-wolf-snow.jpg.adapt.945.1.jpg"
    
    results = c.parse(url)
    print(results)
