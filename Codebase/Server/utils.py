import datetime

def parse_datetime(datetime_str):
    date, time = tuple(datetime_str.split('_'))
    year, month, day = tuple(map(int, date.split('-')))
    hour, minute, second, milli = tuple(map(int, time.split('-')))
    return datetime.datetime(year, month, day, hour, minute, second, milli)