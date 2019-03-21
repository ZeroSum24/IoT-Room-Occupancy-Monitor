#!/usr/bin/env node

const admin = require('firebase-admin');
var serviceAccount = require('./iot-app-3386d-firebase-adminsdk-notbj-6b009a7f9d.json');

//our imports
var chair_cal = require('./chair_calculations')
// inititalise the js server
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseId: 'https://iot-app-3386d.firebaseio.com',
});
var db = admin.firestore();

// functions

function databaseWatcher(query, callback_function) {

  var observer = query.onSnapshot(snapshot => {
    console.log(`Received query snapshot of size ${snapshot.size}`);
    snapshot.docChanges().forEach(function(change) {
            if (change.type === "added") {
                console.log("New city: ", change.doc.data());
                callback_function()
            }
            console.log(change.doc.id)
        });
  }, err => {
    console.log(`Encountered error: ${err}`);
  });
}

// callback functions

function chair_callback() {
  console.log("chair - success");
}

function door_callback() {
  console.log("door - success");
}

function table_callback() {
  console.log("table - success");
}

// inititialise chair database watchers
var chairOneQuery = db.collection('chair_data').doc('SonicWaves-C-001').collection('detections');
var chairTwoQuery = db.collection('chair_data').doc('SonicWaves-C-002').collection('detections');
var chairThreeQuery = db.collection('chair_data').doc('SonicWaves-C-003').collection('detections');
databaseWatcher(chairOneQuery, chair_callback) //chair one watcher
databaseWatcher(chairTwoQuery, chair_callback) //chair one watcher
databaseWatcher(chairThreeQuery, chair_callback) //chair one watcher

// inititialise door database watchers
var doorOneQuery = db.collection('door_data').doc('SonicWaves-D-001').collection('detections');
databaseWatcher(doorOneQuery, door_callback) //door one watcher

// inititialise table database watchers
var tableOneQuery = db.collection('table_data').doc('SonicWaves-T-001').collection('detections');
var tableTwoQuery = db.collection('table_data').doc('SonicWaves-T-002').collection('detections');
databaseWatcher(tableOneQuery, table_callback) //table one watcher
databaseWatcher(tableTwoQuery, table_callback) //table one watcher
