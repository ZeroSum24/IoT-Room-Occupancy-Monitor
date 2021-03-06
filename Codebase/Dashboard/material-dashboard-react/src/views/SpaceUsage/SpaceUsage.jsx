import React from "react";
import PropTypes from "prop-types";
// react plugin for creating charts
import ChartistGraph from "react-chartist";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
// core components
import GridItem from "../../components/Grid/GridItem.jsx";
import GridContainer from "../../components/Grid/GridContainer.jsx";
import Card from "../../components/Card/Card.jsx";
import CardHeader from "../../components/Card/CardHeader.jsx";
import CardBody from "../../components/Card/CardBody.jsx";
import Paper from "@material-ui/core/Paper";

import iconsStyle from "../../assets/jss/material-dashboard-react/views/iconsStyle.jsx";

import Firebase, { FirebaseContext } from '../../firebase';
import { withFirebase } from '../../firebase';

import {
  occupancyStatsChart,
  spaceUsageChart,
  roomUsageChart
} from "../../variables/chartsDetails.jsx";

const styles = {
  cardCategoryWhite: {
    "&,& a,& a:hover,& a:focus": {
      color: "rgba(255,255,255,.62)",
      margin: "0",
      fontSize: "14px",
      marginTop: "0",
      marginBottom: "0"
    },
    "& a,& a:hover,& a:focus": {
      color: "#FFFFFF"
    }
  },
  cardTitleWhite: {
    color: "#FFFFFF",
    marginTop: "0px",
    minHeight: "auto",
    fontWeight: "300",
    fontFamily: "'Roboto', 'Helvetica', 'Arial', sans-serif",
    marginBottom: "3px",
    textDecoration: "none",
    "& small": {
      color: "#777",
      fontSize: "65%",
      fontWeight: "400",
      lineHeight: "1"
    }
  }
};

class SpaceUsage extends React.Component  {

  constructor(props) {
    super(props);
    this.state = {
      roomOccupancyContents: "23 ",
      chairsFreeContents: "49/50",
      mostPopularTimeContents: "12AM",
      totalOccupancyContents: "+234",
      roomUsageData: [[23, 75, 45, 30, 28, 24, 20, 19]],
      spaceUsageData: [[54, 43]],
      occupancyStatsData: [[12, 17, 7, 17, 23, 18, 38]],
    };
  }

  componentDidMount() {
      this.props.firebase.db.collection("data-visual").doc("dashboard_charts").get().then(doc => {
        console.log(doc.id, " => ", doc.data());
        this.setState({
          roomUsageData: [doc.data()['room_usage']],
          spaceUsageData: [doc.data()['space_usage']],
          occupancyStatsData: [doc.data()['occupancy_stats']],
         });
      });
    }

  handleChange = (event, value) => {
    this.setState({ value });
    // this.setState({xScale: ReactD3.time.scale().domain([extent[0], extent[1]]).range([0, 400 - 70])});
  };

  render() {
    const { classes } = this.props;

    return (
      <GridContainer>
        <GridItem xs={12} sm={12} md={12}>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>Space Usage</h4>
              <p className={classes.cardCategoryWhite}>
                Derived from room data per hour
              </p>
            </CardHeader>
            <CardBody>
              <Paper style={{background: "#FC9006", padding:100}}>
                <ChartistGraph
                  className="ct-chart"
                  data={spaceUsageChart(this.state.spaceUsageData).data}
                  type="Bar"
                  options={spaceUsageChart().options}
                  responsiveOptions={spaceUsageChart().responsiveOptions}
                  listener={spaceUsageChart().animation}
                />
              </Paper>
            </CardBody>
          </Card>
        </GridItem>
        <GridItem xs={12} sm={12} md={12}>
            <Card plain>

              <CardBody>

              </CardBody>
            </Card>
        </GridItem>
      </GridContainer>
    );
  }
}

SpaceUsage.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withFirebase(withStyles(iconsStyle)(SpaceUsage));
