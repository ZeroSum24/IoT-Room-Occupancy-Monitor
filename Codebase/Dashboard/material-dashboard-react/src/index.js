import React from "react";
import ReactDOM from "react-dom";
import { createBrowserHistory } from "history";
import { Router, Route, Switch, Redirect } from "react-router-dom";

// core components
import Admin from "./layouts/Admin.jsx";
import RTL from "./layouts/RTL.jsx";
import Firebase, { FirebaseContext } from './firebase';

import "./assets/css/material-dashboard-react.css";

const hist = createBrowserHistory();

// connect the app to firebase
// var cred = credentials.Certificate('../../iot-app-3386d-firebase-adminsdk-notbj-6b009a7f9d.json')
// firebase_admin.initialize_app(cred)

ReactDOM.render(
  <FirebaseContext.Provider value={new Firebase()}>
    <Router history={hist}>
      <Switch>
        <Route path="/admin" component={Admin} />
        <Route path="/rtl" component={RTL} />
        <Redirect from="/" to="/admin/dashboard" />
      </Switch>
    </Router>,
  </FirebaseContext.Provider>,
  document.getElementById("root")
);
