var utils = require('./utils')

module.exports = {
  chair_analysis,
}

// takes in chair id and a list of detections (which is a dictionary with each field value as the key)
// assumes the readings list is ordered into true/false pairs based on the timestamp
function chair_analysis(readingsList) {

    var currently_used = false
    var templateDict = {initialTimestamp: "", finalTimestamp: "", duration: 0}
    var chair_usage = []
    var i = 0

    console.log("CHAIR ANALYSIS readings", readingsList)
    while(i < readingsList.length-1){
        console.log("After iteration", i);
        console.log("Readings", readingsList);

        if (readingsList[i]['activated'] && !readingsList[i+1]['activated']) {
            templateDict["initialTimestamp"] = readingsList[i]['timestamp']
            templateDict["finalTimestamp"] = readingsList[i+1]['timestamp']

            //TODO
            templateDict["duration"] = utils.calculate_time_diff(readingsList[i]['timestamp'], readingsList[i+1]['timestamp'])
            chair_usage.push(templateDict)

            // remove the used readings
            readingsList.splice(i, 2)
        }
        i++;
    }

    console.log("CALC LIST", readingsList)

    // if the last item does not have a pair and it is activated then it is currently_used
    if (readingsList.length >= 1) {
        if (readingsList[readingsList.length-1]['activated']) {
            currently_used = true
        }
    }

    console.log("cal out", currently_used, chair_usage)
    return [currently_used, chair_usage]
}
