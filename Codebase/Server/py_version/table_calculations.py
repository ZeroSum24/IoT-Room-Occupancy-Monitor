
# Return appropriate visualization data
def table_analysis(table_id, readingsList):
    registered_chairs = {'chair1': 0, 'chair2': 0, 'chair3': 0}
    final_reading = readingsList[len(readingsList)-1]


    for reading in readingsList:

        chair_id = reading['chair_id']
        inverse_signal_strength = reading['signal_strength']
        if inverse_signal_strength < 65:
            # note that high inverse_signal_strength means chair is further away
            registered_chairs['chair'+str(chair_id)] = 1
    return (current_chairs, historical_chairs)

def set_tables(chair_dict):
    pass
