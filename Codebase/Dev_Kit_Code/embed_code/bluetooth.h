extern char DEVICE_NAME[];
extern EventQueue eventQueue;
extern uint8_t hrmCounter;
extern HeartRateService *hrServicePtr;

void onBleInitError(BLE &ble, ble_error_t error);

void bleInitComplete(BLE::InitializationCompleteCallbackContext *params);

void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context);
