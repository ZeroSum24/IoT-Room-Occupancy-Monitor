

# takes in chair id and a list of detections (which is a dictionary with each field value as the key)
def chair_analysis(chair_id, readingsList):

    cur_chairs = []
    templateDict = {"initialTimestamp": "", "finalTimestamp": "", "duration": ""}
    chair_usage = [{"initialTimestamp": "", "finalTimestamp": "", "duration": ""}]
    sitting_count = len(cur_chairs)

    while(i+1 < len(readingsList)) {

        if (readingsList[i]['activated'] && !readingsList[i+1]['activated']) {
            templateDict["initialTimestamp"] = readingsList[i]['timestamp']
            templateDict["finalTimestamp"] = readingsList[i+1]['timestamp']

            duration = parseDateTime(readingsList[i+1]['timestamp']) - parseDateTime(readingsList[i]['timestamp'])
            templateDict["duration"] = duration

            chair_usage.append(templateDict)

            # remove the used readings
            readingsList.remove(i)
            readingsList.remove(i+1)
        }
    }

    return (sitting_count, cur_chairs, chair_usage)
