import React from "react";
import PropTypes from "prop-types";
// react plugin for creating charts
import ChartistGraph from "react-chartist";
// @material-ui/core
import withStyles from "@material-ui/core/styles/withStyles";
import Icon from "@material-ui/core/Icon";
// @material-ui/icons
import Store from "@material-ui/icons/Store";
import Warning from "@material-ui/icons/Warning";
import DateRange from "@material-ui/icons/DateRange";
import LocalOffer from "@material-ui/icons/LocalOffer";
import Update from "@material-ui/icons/Update";
import ArrowUpward from "@material-ui/icons/ArrowUpward";
import AccessTime from "@material-ui/icons/AccessTime";
import Accessibility from "@material-ui/icons/Accessibility";
import BugReport from "@material-ui/icons/BugReport";
import Code from "@material-ui/icons/Code";
import Cloud from "@material-ui/icons/Cloud";
// core components
import GridItem from "../../components/Grid/GridItem.jsx";
import GridContainer from "../../components/Grid/GridContainer.jsx";
import Table from "../../components/Table/Table.jsx";
import Tasks from "../../components/Tasks/Tasks.jsx";
import CustomTabs from "../../components/CustomTabs/CustomTabs.jsx";
import Danger from "../../components/Typography/Danger.jsx";
import Card from "../../components/Card/Card.jsx";
import CardHeader from "../../components/Card/CardHeader.jsx";
import CardIcon from "../../components/Card/CardIcon.jsx";
import CardBody from "../../components/Card/CardBody.jsx";
import CardFooter from "../../components/Card/CardFooter.jsx";

import { issues, maintenance, digital } from "../../variables/general.jsx";

import Firebase, { FirebaseContext } from '../../firebase';
import { withFirebase } from '../../firebase';


import {
  chairUsageChart,
  tableUsageChart,
} from "../../variables/tableCharts.jsx";

import dashboardStyle from "../../assets/jss/material-dashboard-react/views/dashboardStyle.jsx";

class TableList extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      chairsFreeContents: "1 ",
      missingChairs: "1/3",
      mostPopularTable: "Table 5 - 12AM",
      chair_plural: "Chair",
      chairUsageData: [[12, 17, 7, 17, 23, 18, 38]],
      tableUsageData: [[54, 43]],
    };
  }

  componentDidMount() {
      this.props.firebase.db.collection("data-visual").doc("current_occupancy").get().then(doc => {
        console.log(doc.id, " => ", doc.data());
        let chairName = "Chair";
        if (doc.data()['chairs'] > 1 || doc.data()['chairs'] === 0) {
          chairName = "Chairs"
        }
        this.setState({
          chairsFreeContents: (doc.data()['chairs'].toString() + " "),
          missingChairs: (doc.data()['missing_chairs'].toString()+"/3"),
          chair_plural: chairName,
         });
      });
      //
      this.props.firebase.db.collection("data-visual").doc("history_info").get().then(doc => {
        console.log(doc.id, " => ", doc.data());
        this.setState({
          mostPopularTable: doc.data()['most_popular_table'].toString(),
         });
      });

      this.props.firebase.db.collection("data-visual").doc("dashboard_charts").get().then(doc => {
        console.log(doc.id, " => ", doc.data());
        this.setState({
          chairUsageData: [doc.data()['chair_stats']],
          tableUsageData: [doc.data()['table_stats']],
         });
      });
    }

  handleChange = (event, value) => {
    this.setState({ value });
  };

  handleChangeIndex = index => {
    this.setState({ value: index });
  };

  render() {
    const { classes } = this.props;
    return (
        <div>
          <GridContainer>
            <GridItem xs={12} sm={6} md={4}>
              <Card>
                <CardHeader color="warning" stats icon>
                  <CardIcon color="warning">
                    <Icon>content_copy</Icon>
                  </CardIcon>
                  <p className={classes.cardCategory}>Current Chairs Free</p>
                  <h3 className={classes.cardTitle}>
                    {this.state.chairsFreeContents}<small>{this.state.chair_plural}</small>
                  </h3>
                </CardHeader>
                <CardFooter stats>
                  <div className={classes.stats}>
                    <Danger>
                      <Warning />
                    </Danger>
                    <a href="#pablo" onClick={e => e.preventDefault()}>
                      Get more space
                    </a>
                  </div>
                </CardFooter>
              </Card>
            </GridItem>
            <GridItem xs={12} sm={6} md={4}>
              <Card>
                <CardHeader color="success" stats icon>
                  <CardIcon color="success">
                    <Store />
                  </CardIcon>
                  <p className={classes.cardCategory}>Missing Chairs</p>
                  <h3 className={classes.cardTitle}>
                  {this.state.missingChairs}</h3>
                </CardHeader>
                <CardFooter stats>
                  <div className={classes.stats}>
                    <DateRange />
                    Last 24 Hours
                  </div>
                </CardFooter>
              </Card>
            </GridItem>
            <GridItem xs={12} sm={6} md={4}>
                  <Card>
                    <CardHeader color="danger" stats icon>
                      <CardIcon color="danger">
                        <Icon>info_outline</Icon>
                      </CardIcon>
                      <p className={classes.cardCategory}>Most Popular Table</p>
                      <h3 className={classes.cardTitle}>
                      {this.state.mostPopularTable}</h3>
                    </CardHeader>
                    <CardFooter stats>
                      <div className={classes.stats}>
                        <LocalOffer />
                        Tracked from Github
                      </div>
                    </CardFooter>
                  </Card>
            </GridItem>
          </GridContainer>
          <GridContainer>
            <GridItem xs={12} sm={12} md={6}>
              <Card chart>
                <CardHeader color="success">
                  <ChartistGraph
                    className="ct-chart"
                    data={chairUsageChart(this.state.chairUsageData).data}
                    type="Line"
                    options={chairUsageChart().options}
                    listener={chairUsageChart().animation}
                  />
                </CardHeader>
                <CardBody>
                  <h4 className={classes.cardTitle}>Chair Usage Statistics</h4>
                  <p className={classes.cardCategory}>
                    <span className={classes.successText}>
                      <ArrowUpward className={classes.upArrowCardCategory} /> 55%
                    </span>{" "}
                    Amount of chairs used across the week
                  </p>
                </CardBody>
                <CardFooter chart>
                  <div className={classes.stats}>
                    <AccessTime /> campaign sent 2 days ago
                  </div>
                </CardFooter>
              </Card>
            </GridItem>
            <GridItem xs={12} sm={12} md={6}>
              <Card chart>
                <CardHeader color="warning">
                  <ChartistGraph
                    className="ct-chart"
                    data={tableUsageChart(this.state.tableUsageData).data}
                    type="Bar"
                    options={tableUsageChart().options}
                    responsiveOptions={tableUsageChart().responsiveOptions}
                    listener={tableUsageChart().animation}
                  />
                </CardHeader>
                <CardBody>
                  <h4 className={classes.cardTitle}>Table Usage</h4>
                  <p className={classes.cardCategory}>
                    Which tables have been used most over the last week
                  </p>
                </CardBody>
                <CardFooter chart>
                  <div className={classes.stats}>
                    <AccessTime /> updated 4 minutes ago
                  </div>
                </CardFooter>
              </Card>
            </GridItem>
          </GridContainer>
        </div>

    );
  }
}

TableList.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withFirebase(withStyles(dashboardStyle)(TableList));
