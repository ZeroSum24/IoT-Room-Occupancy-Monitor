module.exports = {
  door_analysis,
}

function door_analysis(readingsList) {
    var net_movement = 0;
    var readingsDict = readingsList[0];
    for(var i = 0; i < readingsList.length; i++) {
        if (readingsDict[i]['entered']) {
            net_movement += 1
        }
        else {
            net_movement -= 1
        }
    }

    return net_movement
}
