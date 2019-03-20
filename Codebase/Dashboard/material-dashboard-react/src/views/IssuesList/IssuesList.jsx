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
  occupancyStatsChart,
  spaceUsageChart,
  roomUsageChart
} from "../../variables/tableCharts.jsx";

import dashboardStyle from "../../assets/jss/material-dashboard-react/views/dashboardStyle.jsx";

class IssuesList extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      roomOccupancyContents: "1 ",
      chairsFreeContents: "49/50",
      mostPopularTimeContents: "Table 5 @ 12AM",
      totalOccupancyContents: "+234",
      roomUsageData: [[23, 75, 45, 30, 28, 24, 20, 19]],
      spaceUsageData: [[54, 43]],
      occupancyStatsData: [[12, 17, 7, 17, 23, 18, 38]],
    };
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
          <GridItem xs={12} sm={12} md={6}>
            <CustomTabs
              title="Tasks:"
              headerColor="primary"
              tabs={[
                {
                  tabName: "Issues",
                  tabIcon: BugReport,
                  tabContent: (
                    <Tasks
                      checkedIndexes={[0, 3]}
                      tasksIndexes={[0, 1, 2, 3]}
                      tasks={issues}
                    />
                  )
                },
                {
                  tabName: "Maintenance",
                  tabIcon: Code,
                  tabContent: (
                    <Tasks
                      checkedIndexes={[0]}
                      tasksIndexes={[0, 1]}
                      tasks={maintenance}
                    />
                  )
                },
                {
                  tabName: "Online Status",
                  tabIcon: Cloud,
                  tabContent: (
                    <Tasks
                      checkedIndexes={[1]}
                      tasksIndexes={[0, 1, 2]}
                      tasks={digital}
                    />
                  )
                }
              ]}
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={6}>
            <Card>
              <CardHeader color="warning">
                <h4 className={classes.cardTitleWhite}>Missing Chair Details</h4>
                <p className={classes.cardCategoryWhite}>
                  A list of missing chairs in the room
                </p>
              </CardHeader>
              <CardBody>
                <Table
                  tableHeaderColor="warning"
                  tableHead={["ID", "TableName", "Last associated"]}
                  tableData={[
                    ["007", "Table 54", "26/08/2019 12:34AM"],
                    ["032", "Table 12", "13/03/2019 16:19PM"],
                    ["041", "Table 09", "26/08/2019 17:34PM"],
                    ["015", "Table 03", "21/08/2019 11:21AM"],
                  ]}
                />
              </CardBody>
            </Card>
          </GridItem>
        </GridContainer>
        </div>

    );
  }
}

IssuesList.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withFirebase(withStyles(dashboardStyle)(IssuesList));
