from flask import Flask
from wiki import WikiParse
app = Flask(__name__)


@app.route("/")
def hello():
    return "Beware of jeff"

@app.route("/find/<path:url>")
def find(url):
    w = WikiParse()    
    matches = w.get_matches(url)
    
    return matches

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)
