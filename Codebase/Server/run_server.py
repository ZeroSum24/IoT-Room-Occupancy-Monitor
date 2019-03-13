#!/usr/bin/env python3
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

def init_firestore():
    # Using a service account credentials to verify the user
    cred = credentials.Certificate('./iot-app-3386d-firebase-adminsdk-notbj-6b009a7f9d.json')
    firebase_admin.initialize_app(cred)


def getFirebaseData():
    chairs_ref = db.collection(u'chair_data')
    docs = chairs_ref.get()

    for doc in docs:
        print(u'{} => {}'.format(doc.id, doc.to_dict()))


def setFirebaseData():
        doc_ref = db.collection(u'data-visual').document(u'room_occupancy')
        doc_ref.set({
            'current_occupants': 4
        })

if __name__ == '__main__':

    init_firestore()
    db = firestore.client()

    print("Blank atm")
    # getFirebaseData()
    # setFirebaseData()
