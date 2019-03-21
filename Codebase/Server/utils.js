
module.exports = {
  filter_sort_data,
}

function filter_sort_data(readingsList, last_timestamp) {
    data = []

    // set the largest timestamp if still default
    if (last_timestamp == 0) {
      last_timestamp = new Date(year=0, month=1, day=1);
    }
    var timestamp;
    var largest_timestamp = new Date(year=0, month=1, day=1);

    // iterate over the indexes of the dictionary
    for (index in readingsList) {
      var reading = readingsList[index]
      timestamp = reading['timestamp']

      readingDate = parse_timestamp(timestamp)
      if (readingDate >= last_timestamp) {
          if (readingDate > largest_timestamp) {
              largest_timestamp = readingDate;
            }
          data.push([reading, readingDate]);
        }
      }
    data.sort(function(a,b) {
      return a[1] - b[1];
    })

    // only return the first element of each 2d array
    output_dict = {}
    for (var i = 0; i < data.length; i++) {
      output_dict[i] = data[i][0]
    }
    console.log("filter working")
    console.log([output_dict, largest_timestamp])
    return [output_dict, largest_timestamp]
  }

// parses a string into a datetime object
function parse_timestamp(datetime_str) {
    var [date, time] = datetime_str.split('_')

    // calculate date
    var [year, month, day] = date.split('-')
    year = parseInt(year)
    month = parseInt(month)
    day = parseInt(day)

    // calculate time
    var [hour, minute, second, milli] = time.split('-')
    hour = parseInt(hour)
    minute = parseInt(minute)
    second = parseInt(second)
    milli = parseInt(milli)

    //returns the date from parsing
    return (new Date(year, month, day, hour, minute, second, milli));
  }

sortArrayOfObjects = (arr, key) => {
  return arr.sort((a, b) => {
      return a[key] - b[key];
  });
};
