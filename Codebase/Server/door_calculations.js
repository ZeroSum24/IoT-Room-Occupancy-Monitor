function door_analysis(readingsList) {
    var net_movement = 0

    for(var i = 0; i < readingsList.length; i++) {
        if (readingsList[i]['entered']) {
            net_movement += 1
        }
        else {
            net_movement -= 1
        }
    }

    return net_movement
}