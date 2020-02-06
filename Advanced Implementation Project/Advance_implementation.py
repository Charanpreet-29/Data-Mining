from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
import ssl
import re
import requests
import math


def build_root_set(search_term):
    url = "https://www.google.com/search?q="+search_term+"&num=35" #Getting 35 results to compensate for duplicates if any
    raw_page = requests.get(url, headers=header).text
    results = re.findall(r'(?<=<h3 class="r"><a href="/url\?q=).*?(?=&amp)', str(raw_page))
    #k = int(input ("Enter number"))
    return list(set(results))[0:30] #Provides 30 unique of the 35 we requested above

def searchGoogle(query):
    #use google library to search for the key word
    search_results = search(query,tld="com", num=30, stop=30);
    RootSet=[]
    for page in search_results:
        RootSet.append(page)
    return RootSet;

def print_desccriptions(neighborhood):
    descriptions = []

    for page in neighborhood:
        req = Request(page, headers=header)
        html = urlopen(req)
        soup = BeautifulSoup(html)

        title = soup.title.text
        metas = soup.find_all("meta")
        desc = [ meta.attrs['content'] for meta in metas if 'name' in meta.attrs and meta.attrs['name'] == 'description' ]

        if len(title) < 1:
            title = re.findall(r'^(?:https?:\/\/)?(?:[^@\/\n]+@)?(?:www\.)?([^:\/\n]+)', page)
            title = title[0]
        if len(desc) < 1:
            desc = soup.p.text
        else:
            desc = desc[0]

        if len(desc) > 140:
            desc = desc[0:140]+'...'

        descriptions.append([title, page, desc])
  
    for page in descriptions:
        print("\n")
        print(page[0],page[1],page[2],'\n\n',sep='\n')

    print_desccriptions(neighborhood)

header = {'user-agent': 'Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11'}
ssl._create_default_https_context = ssl._create_unverified_context
root_set = build_root_set('Deep+Learning') #Note that spaces have to be replaced with '+'
print(" \n  \t printing  \n \t ")
print("Printing Root_Set for Deep Learning")
for item in root_set:
    print(" \n  \t root sets \n \t ")
    print(item,"\n")
    
	
neighborhood_set = []
for root in root_set:
	neighborhood_set.append(build_root_set(root))
	print("Neighbourhood set: ", neighborhood_set)

req = Request("https://www.deeplearning.ai/")
html_page = urlopen(req)

soup = BeautifulSoup(html_page, "lxml")

links = []
for link in soup.findAll('a'):
    item = str(link.get('href'))
    if "http" in item:
        links.append(item)

print(" \n  \n  ")
print(links)


G = links
for p in G:
    auth = 1 
    hub = 1 
def HubsAndAuthorities(G):
         norm = 0
         for p in G:  
            auth = 0
            print("Hi val : ",p)
            for q in root_set: 
                 auth += hub
                 print("q is :", q)
                 print("auth:",auth)
            norm += math.sqrt(auth) 
            print("norm :", norm) 
         norm =math.sqrt(norm)
         for p in G: 
             auth = int(auth) / float(norm) 
         norm = 0
         for p in G :  
             hub = 0
             for r in neighborhood_set :
                 hub += auth
             norm += math.sqrt(hub) 
         norm = math.sqrt(norm)
         for p in G :  
             hub =int(hub) / float(norm)  
print ("Hubs: ", hub);
print ("authorities: ", auth)


HubsAndAuthorities(G)			 
