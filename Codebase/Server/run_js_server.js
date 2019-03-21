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
let chair_devices = ['SonicWaves-C-001', 'SonicWaves-C-002','SonicWaves-C-003']

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
  console.log("test", updated_chair_readings)
  console.log("test2", last_read_timestamps[device_name])
  var [current_flag, hist_chairs] = chair_cal.chair_analysis(updated_chair_readings)

  // update the chair flags in data-visual
  var chairRef = db.collection('data-visual').doc('current_occupancy');
  send_dict = {}; send_dict[device_name] = current_flag;
  var setWithOptions = chairRef.set(send_dict, {merge: true});

  //update the information in data-visual, historical and otherwise
  calculate_current_chairs() // update the current chairs count

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
  var out_array = utils.filter_sort_data(updated_door_readings, last_read_timestamps[device_name])
  updated_door_readings = out_array[0];
  last_read_timestamps[device_name] = out_array[1];
  console.log("DOOR TEST", updated_door_readings)
  console.log("DOOR TIMESTAMPS", last_read_timestamps[device_name])
  net_movement = door_cal.door_analysis(updated_door_readings);

  calculate_current_occupancy(net_movement);
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

function calculate_current_occupancy(net_movement) {
  var occupancy = 0;
  var cur_occ_ref = db.collection('data-visual').doc('current_occupancy')
  var getDoc = cur_occ_ref.get()
  .then(doc => {
    if (!doc.exists) {
      console.log('No such document!');
    } else {
      console.log("cur occupancy val", doc.data().occupants)
      occupancy = doc.data().occupants + net_movement;
      if (occupancy < 0) {
        occupancy = 0;
      }
    }
  })
  .then( () => {
      cur_occ_ref.set({occupants: occupancy}, {merge: true});
      // update day history file here
      // if net positive then update the total occupancy
      update_total_occupancy(net_movement)
    }
  )
  .catch(err => {
    console.log('Error getting document', err);
  });
}

function update_total_occupancy(net_movement) {
  var total_occupants = 0
  var cur_occ_ref = db.collection('data-visual').doc('history_info')
  var getDoc = cur_occ_ref.get()
  .then(doc => {
    if (!doc.exists) {
      console.log('No such document!');
    } else {
      total_occupants = doc.data().total_occupancy + net_movement;
    }
  })
  .then( () => {
      if (net_movement > 0) {
        cur_occ_ref.set({total_occupancy: total_occupants}, {merge: true});
      }
    }
  )
  .catch(err => {
    console.log('Error getting document', err);
  });
}

function calculate_current_chairs() {
  var chair_count = 0;
  var cur_occ_ref = db.collection('data-visual').doc('current_occupancy')
  var getDoc = cur_occ_ref.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('No such document!');
      } else {
        console.log('Document data:', doc.data());
        for (field in doc.data()) {
          // for the chair fields, if they are true, update the chair count
          if (chair_devices.includes(field) && doc.data()[field]) {
            chair_count++;
          }
        }
      }
    })
    .then( () => {
      cur_occ_ref.set({chairs: chair_count}, {merge: true})
//      updateHistoricalChairsWeeks(chair_count)
      // update day history file here
      }
    )
    .catch(err => {
      console.log('Error getting document', err);
    });
}

function updateHistoricalChairsWeeks(cur_chair_count) {
  var average_chair_count = 0;
  var num_updates = 0;
  var hist_cur_chairs_ref = db.collection('data-visual').doc('average_week')
                              .collection(utils.getCurrentDay()).doc('average_chair_count')
  var getDoc = hist_cur_chairs_ref.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('No such document!');
      } else {
        console.log('Document data:', doc.data());
        average_chair_count = doc.data()['average_chair_count']
        num_updates = doc.data()['num_updates']
        var total_chairs = average_chair_count * num_updates;
        num_updates++;
        average_chair_count = Math.floor((total_chairs + cur_chair_count) / num_updates);
        }
      }
    )
    .then( () => {
      hist_cur_chairs_ref.set({average_chair_count: average_chair_count,
        num_updates: num_updates}, {merge: true})
      }
    )
    .catch(err => {
      console.log('Error getting document', err);
    });
}

function getDataVisual(device_name) {

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
