// ##############################
// // // Tasks for TasksCard - see Dashboard view
// #############################

var issues = [
  'Critical: Pipe has burst in room and flooded the floor - swimming pool added to lab',
  "Students have covered one of the walls with intricate monitoring hardware",
  "Replace the flip tables in the room (after that one student lost a finger)",
  "Variable heat and lighting conditions are causing coldness in certain room areas"
];
var maintenance = [
  "Water hoover ordered for the room",
  'Sign contract to purchase replacement machines'
];
var digital = [
  "Online service is up and running",
  "Server testing reported as successful",
  'Devices register record data collection'
];

module.exports = {
  // these 3 are used to create the tasks lists in TasksCard - Dashboard view
  issues,
  maintenance,
  digital
};
