var utils = require('./utils')

module.exports = {
  chair_analysis,
}

// takes in chair id and a list of detections (which is a dictionary with each field value as the key)
// assumes the readings list is ordered into true/false pairs based on the timestamp
function chair_analysis(readingsDict) {

    var currently_used = false
    var templateDict = {initialTimestamp: "", finalTimestamp: "", duration: 0}
    var chair_usage = []
    var i = 0

    console.log("here")
    while(i < (Object.keys(readingsDict).length)-1){
        console.log("here2")

        if (readingsDict[i]['activated'] && !readingsDict[i+1]['activated']) {
            templateDict["initialTimestamp"] = readingsDict[i]['timestamp']
            templateDict["finalTimestamp"] = readingsDict[i+1]['timestamp']

            //TODO
            templateDict["duration"] = utils.calculate_time_diff(readingsDict[i]['timestamp'], readingsDict[i+1]['timestamp'])
            chair_usage.push(templateDict)

            // remove the used readings
            delete readingsDict[i]
            delete readingsDict[i+1]
        }
        i++;
    }

    console.log("CALC LIST", readingsDict)

    var amnt_vals = (Object.keys(readingsDict).length)

    // if the last item does not have a pair and it is activated then it is currently_used
    if (amnt_vals >= 1) {
        if (readingsDict[amnt_vals-1]['activated'] != undefined) {
          if (readingsDict[amnt_vals-1]['activated']) {
              currently_used = true
          }
      }
    }

    console.log("cal out", currently_used, chair_usage)
    return [currently_used, chair_usage]
}
