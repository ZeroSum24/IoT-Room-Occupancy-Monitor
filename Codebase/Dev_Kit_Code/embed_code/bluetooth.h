void onBleInitError(BLE &ble, ble_error_t error);

void bleInitComplete(BLE::InitializationCompleteCallbackContext *params);

void scheduleBleEventsProcessing(BLE::OnEventsToProcessCallbackContext* context);
