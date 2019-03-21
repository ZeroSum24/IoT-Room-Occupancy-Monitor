#!/usr/bin/env node

const admin = require('firebase-admin');
var serviceAccount = require('./iot-app-3386d-firebase-adminsdk-notbj-6b009a7f9d.json');

//our imports
var utils = require('./utils')
var chair_cal = require('./chair_calculations')
var door_cal = require('./door_calculations')
var table_cal = require('./table_calculations')
//


let last_read_timestamps = {'SonicWaves-C-001': 0, 'SonicWaves-C-002':0, 'SonicWaves-C-003':0,
                            'SonicWaves-T-001': 0, 'SonicWaves-T-002':0, 'SonicWaves-D-001': 0}

// inititalise the js server
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseId: 'https://iot-app-3386d.firebaseio.com',
});
var db = admin.firestore();

// functions

function databaseWatcher(query, callback_function, device_name) {

  var observer = query.onSnapshot(snapshot => {
    console.log(`Received query snapshot of size ${snapshot.size}`);
    callback_function(snapshot, device_name);
  }, err => {
    console.log(`Encountered error: ${err}`);
  });
}

// callback functions

function chair_callback(snapshot, device_name) {
  // converts all the updated readings into a javascript object
  var updated_chair_readings = {}
  snapshot.docChanges().forEach(function(change) {
          if (change.type === "added") {
              console.log(device_name, "chair reading: ", change.doc.id, change.doc.data());
              updated_chair_readings[change.doc.id] = change.doc.data()
          }
      });
  // filter and sort the data
  var out_array = utils.filter_sort_data(updated_chair_readings, last_read_timestamps[device_name])
  updated_chair_readings = out_array[0]
  last_read_timestamps[device_name] = out_array[1]
  var [hist_chairs, current_flag] = chair_cal.chair_analysis(updated_chair_readings)

  // update the chair flags in data-visual
  var chairRef = db.collection('data-visual').doc('current_occupancy');
  send_dict = {}; send_dict[device_name] = out_array[0];
  var setWithOptions = chairRef.set(send_dict, {merge: true});

  //update the information in data-visual, historical and otherwise

  // callback_function(snapshot)
}

function door_callback(snapshot, device_name) {
  // converts all the updated readings into a javascript object
  var updated_door_readings = {}
  snapshot.docChanges().forEach(function(change) {
          if (change.type === "added") {
              console.log(device_name, "door reading: ", change.doc.id, change.doc.data());
              updated_door_readings[change.doc.id] = change.doc.data()
          }
      });
  // callback_function()
}

function table_callback(snapshot, device_name) {
  // converts all the updated readings into a javascript object
  var updated_table_readings = {}
  snapshot.docChanges().forEach(function(change) {
          if (change.type === "added") {
              console.log(device_name, "table reading: ", change.doc.id, change.doc.data());
              updated_table_readings[change.doc.id] = change.doc.data()
          }
      });
  // callback_function()
}

function getDataVisual(device_name) {
  var cityRef = db.collection('data-visual').doc('current_occupancy')
  var getDoc = cityRef.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('No such document!');
      } else {
        console.log('Document data:', doc.data());
      }
    })
    .catch(err => {
      console.log('Error getting document', err);
    });

}

// inititialise chair database watchers
var chairOneQuery = db.collection('chair_data').doc('SonicWaves-C-001').collection('detections');
var chairTwoQuery = db.collection('chair_data').doc('SonicWaves-C-002').collection('detections');
var chairThreeQuery = db.collection('chair_data').doc('SonicWaves-C-003').collection('detections');
databaseWatcher(chairOneQuery, chair_callback, 'SonicWaves-C-001') //chair one watcher
databaseWatcher(chairTwoQuery, chair_callback, 'SonicWaves-C-002') //chair one watcher
databaseWatcher(chairThreeQuery, chair_callback, 'SonicWaves-C-003') //chair one watcher

// inititialise door database watchers
var doorOneQuery = db.collection('door_data').doc('SonicWaves-D-001').collection('detections');
databaseWatcher(doorOneQuery, door_callback, 'SonicWaves-D-001') //door one watcher

// inititialise table database watchers
var tableOneQuery = db.collection('table_data').doc('SonicWaves-T-001').collection('detections');
var tableTwoQuery = db.collection('table_data').doc('SonicWaves-T-002').collection('detections');
databaseWatcher(tableOneQuery, table_callback, 'SonicWaves-T-001') //table one watcher
databaseWatcher(tableTwoQuery, table_callback, 'SonicWaves-T-002') //table one watcher
