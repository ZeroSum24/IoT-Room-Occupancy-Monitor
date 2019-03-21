module.exports = {
  door_analysis,
}

function door_analysis(readingsList) {
    var net_movement = 0;
    for(var i = 0; i < readingsList.length; i++) {
        if (readingsList[i]['activated'] == 1) {
            net_movement += 1
            console.log("HERE2")
        } else {
            net_movement -= 1
        }
    }

    return net_movement
}
