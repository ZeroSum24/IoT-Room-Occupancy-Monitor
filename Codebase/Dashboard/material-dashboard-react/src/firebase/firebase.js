import app from 'firebase/app';
// import firebase from 'firebase';
// import 'firebase/firestore';
import 'firebase/auth';
import 'firebase/firestore';

var config = {
  apiKey: "AIzaSyDAdRuvIgoAaCOVG8dUdahBR_1kQInTLxg",
  authDomain: "iot-app-3386d.firebaseapp.com",
  databaseURL: "https://iot-app-3386d.firebaseio.com",
  projectId: "iot-app-3386d",
  storageBucket: "iot-app-3386d.appspot.com",
  messagingSenderId: "769453940559"
};

class Firebase {
  constructor() {
    app.initializeApp(config);

    // pulling from the firestore
    this.auth = app.auth();
    this.db = app.firestore();

    this.state = {
      average_day: {},
      average_week: {},
      current_occupancy: {},
      last_updated: {},
      room_details: {},
      data: {},
    }

    this.data_visual = this.db.collection("data-visual").get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
      });
    });
  }

  // *** Auth API ***
  doCreateUserWithEmailAndPassword = (email, password) =>
    this.auth.createUserWithEmailAndPassword(email, password);

  doSignInWithEmailAndPassword = (email, password) =>
    this.auth.signInWithEmailAndPassword(email, password);

  doSignOut = () => this.auth.signOut();

  doPasswordReset = email => this.auth.sendPasswordResetEmail(email);

  doPasswordUpdate = password =>
    this.auth.currentUser.updatePassword(password);

  // *** Firestore API ***

  dbGetAverageDay = () => {
    this.db.collection('data_visual').document('average_day').get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
      });
    });
  }

  dbGetAverageWeek = () => {
    this.db.collection('data_visual').document('average_week').get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
      });
    });
  }

  dbGetCurrentOccupancy = () => {
    this.db.collection('data_visual').document('current_occupancy').get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
      });
    });
  }

  dbGetCurrentOccupancy = () => {
    this.db.collection("data-visual").document("current_occupancy").get().then(function(querySnapshot) {
    querySnapshot.forEach(function(field) {
        // doc.data() is never undefined for query doc snapshots
        console.log(field.id, " => ", field.data());
      });
    });
  }

  dbGetRoomDetails = () => {
    this.db.collection('data_visual').document('room_details').get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
      });
    });
  }

  dbCurrentOccupancyRef = () => {
    var ref = this.db.collection("data_visual").doc("current_occupancy");
    return ref
  }

  dbHistoryInfoRef = () => {
    var ref = this.db.collection("data_visual").doc("history_info");
    return ref
  }
}

export default Firebase;
