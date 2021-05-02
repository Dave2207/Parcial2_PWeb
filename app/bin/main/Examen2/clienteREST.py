import requests
import json

class Client():
    def __init__(self, endpoint=''):
        self.endpoint = endpoint
    
    def findAll(self):
        req = requests.get(url = self.endpoint)
        data = req.json()
        for j in data:
            print(j)

    def find(self,id):
        req = requests.get(url = self.endpoint+id)
        data = req.text
        print(data)

    def delete(self,id):
        req = requests.delete(url = self.endpoint+id)

    def insert(self, id, name, lat, lon, user, img):
        data = {'nombre': name, 'latitud': lat, 'longitud': lon, 'usuario': user, 'imagen': img}
        js = json.dumps(data)