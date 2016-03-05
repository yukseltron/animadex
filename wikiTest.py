import wikipedia
import mwparserfromhell

def runTest():
    page = wikipedia.WikipediaPage("Squirrel")
    print(page.links)

def parse():
    site = pywikibot.Site()
    page = pywikibot.Page(site, title)
    text = page.get()
    return mwparserfromhell.parse(text)

runTest()
