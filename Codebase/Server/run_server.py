#!/usr/bin/env python3
import firebase_admin
import time
from firebase_admin import credentials, db
from firebase_admin import firestore
import datetime
import chair_calculations
import door_calculations
import table_calculations

from utils import parse_datetime

last_read_timestamps = {'SonicWaves-C-001': datetime.datetime(year=datetime.MINYEAR, month=1, day=1), 'SonicWaves-C-002':datetime.datetime(year=datetime.MINYEAR, month=1, day=1), 'SonicWaves-C-003':datetime.datetime(year=datetime.MINYEAR, month=1, day=1),
                        'SonicWaves-T-001': datetime.datetime(year=datetime.MINYEAR, month=1, day=1), 'SonicWaves-T-002':datetime.datetime(year=datetime.MINYEAR, month=1, day=1), 'SonicWaves-D-001':datetime.datetime(year=datetime.MINYEAR, month=1, day=1)}

# Used for global access to firebase client
db_pointer = None

def init_firestore():
    # Using a service account credentials to verify the user
    cred = credentials.Certificate('./iot-app-3386d-firebase-adminsdk-notbj-6b009a7f9d.json')
    firebase_admin.initialize_app(cred, {'databaseURL': 'https://iot-app-3386d.firebaseio.com'})


def filter_sort_data(documents, last_timestamp, sensor_name):
    data = []
    largest_timestamp = datetime.datetime(year=datetime.MINYEAR, month=1, day=1)
    for document in documents:
        timestamp = document.id
        doc = document.to_dict() 
        data_datetime = parse_datetime(timestamp)
        if data_datetime > last_timestamp:
            if data_datetime > largest_timestamp:
                largest_timestamp = data_datetime
            data.append(doc)
    last_read_timestamps[sensor_name] = largest_timestamp
    return data


def get_firebase_data(db, name):
    device = "None"
    if '-C-' in name:
        ref = db_pointer.collection('chair_data').document(name).collection('detections')
        device = "chair"
    elif '-T-' in name:
        ref = db_pointer.collection('table_data').document(name).collection('detections')
        device = "table"
    elif '-D-' in name:
        ref = db_pointer.collection('door_data').document(name).collection('detections')
        device = "door"
    else:
        print("Did not recognize name of change")
        return
    docs = ref.get()
        
    data = filter_sort_data(docs, last_read_timestamps[name], name)
    if device == "chair":
        chair_calculations.chair_analysis(name, data)
    elif device == "table":
        table_calculations.chair_analysis(name, data)
    else: # device == door
        door_calculations.chair_analysis(name, data)


def setFirebaseData():
        doc_ref = db.collection(u'data-visual').document(u'room_occupancy')
        doc_ref.set({
            'current_occupants': 4
        })


def calculate_chairs():
    filter_values = last_read_timestamps['']
    pass


def calculate_occupancy(timestamp):
    pass


def calculate_chair_position(timestamp):
    pass


def chair_snapshot(chairs, changes, readTime):
    print("Chair Triggered!")
    for doc in changes:
        print('In Snapshot {}'.format(doc.document.id))
        get_firebase_data(db_pointer, doc.document.id)


def table_snapshot(tables, changes, readTime):
    print("Tables Triggered!")
    for doc in tables:
        print('{}'.format(doc.id))


def door_snapshot(doors, changes, readTime):
    print("Chair Triggered!")
    for doc in doors:
        print('{}'.format(doc.id))


def update_averages(value_dictionary, weeks=True):
    if weeks:
        collection_names = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday']
    else:
        collection_names = []
        for i in range(0, 25):
            if i == 24:
                break
            time = str(i) + '-' + str(i+1)
            collection_names.append(time)
        collection_names.append('24-0')
    for i in collection_names:
        for document_name in ['average_chair_count', 'average_missing_chairs', 'average_table_count', 'total_occupancy']:
            collection = db_pointer.collection('data-visual').document('average_week').collection(i).document(
                document_name)
            values = collection.get().to_dict()
            average_count = values[document_name]
            num_updates = values['num_updates']
            total_count = average_count * num_updates
            total_count += value_dictionary[document_name]
            new_average = total_count / num_updates + 1
            collection.set({document_name: new_average, 'num_updates': num_updates + 1})


if __name__ == '__main__':
    init_firestore()

    db_pointer = firestore.client()
    chair_callback = db_pointer.collection('chair_data').on_snapshot(chair_snapshot)
    door_callback = db_pointer.collection('door_data').on_snapshot(door_snapshot)
    table_callback = db_pointer.collection('table_data').on_snapshot(table_snapshot)

    while True:
        continue
    # update_averages(value_dictionary={}, weeks=True)

    # getFirebaseData()
    # setFirebaseData()