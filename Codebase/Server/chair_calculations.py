from utils import calculate_time_diff

# takes in chair id and a list of detections (which is a dictionary with each field value as the key)
# assumes the readings list is ordered into true/false pairs based on the timestamp
def chair_analysis(readingsList):

    currently_used = False
    templateDict = {"initialTimestamp": "", "finalTimestamp": "", "duration": 0}
    chair_usage = []
    i = 0

    while(i < len(readingsList)-1) {

        if (readingsList[i]['activated'] && !readingsList[i+1]['activated']) {
            templateDict["initialTimestamp"] = readingsList[i]['timestamp']
            templateDict["finalTimestamp"] = readingsList[i+1]['timestamp']

            templateDict["duration"] = calculate_time_diff(readingsList[i]['timestamp'], readingsList[i+1]['timestamp'])
            chair_usage.append(templateDict)

            # remove the used readings
            readingsList.remove(i+1)
            readingsList.remove(i)
        }
        i+=1
    }

    # if the last item does not have a pair and it is activated then it is currently_used
    if (len(readingsList) >= 1):
        if (readingsList[len(readingsList)-1]['activated']):
            currently_used = True

    return (currently_used, chair_usage)
