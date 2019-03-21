import datetime

# parses a string into a datetime object
def parse_datetime(datetime_str):
    date, time = tuple(datetime_str.split('_'))
    year, month, day = tuple(map(int, date.split('-')))
    hour, minute, second, milli = tuple(map(int, time.split('-')))
    return datetime.datetime(year, month, day, hour, minute, second, milli)

# calculates the difference in time between two given string date stamps
def calculate_time_diff(timestamp_one, timestamp_two):
    dateTimeOne = parse_datetime(timestamp_one)
    dateTimeTwo = parse_datetime(timestamp_two)

    return (abs(dateTimeOne-dateTimeTwo))

def calculate_hour_bucket(datetime_obj):
    hour = datetime_obj.hour
    return str(hour) + '-' + str(hour+1)
