package com.renault.garage.exception;

public class VehicleQuotaExceededException extends BusinessException {
    public VehicleQuotaExceededException(Long garageId, int max) {
        super("Quota exceeded: garage " + garageId + " cannot store more than " + max + " vehicles.");
    }
}
