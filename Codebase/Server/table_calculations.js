module.exports = {
  table_analysis,
}

function table_analysis(readingsList) {
    var chair_values = {'chair1': 0, 'chair2': 0, 'chair3': 0};
    var already_read_devices = [];

    // Currently gets the most recent readings from the table
    for (var i = readingsList.length - 1; i > -1; i++)
    {
        chair_id = 'chair' + readingsList[i]['chair_id'].toString();
        // Build up a list of scanned devices.
        // If the device is already in the list then we are looking at old readings so STOP.
        if (already_read_devices.indexOf(chair_id) != -1) {
            return chair_values;
        }
        already_read_devices.push(chair_id);
        if (readingsList[i]['signal_strength'] < 65) {
            chair_values[chair_id] = 1;
        }
    }
    return chair_values;
}
