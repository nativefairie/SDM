package com.sdm.emergency_dispatcher_center.service;

import com.sdm.emergency_dispatcher_center.domain.*;
import com.sdm.emergency_dispatcher_center.repository.LogRepository;
import com.sdm.emergency_dispatcher_center.repository.ReportRepository;
import com.sdm.emergency_dispatcher_center.repository.UserRepository;

import java.util.Date;

public class OperationDto {
    private Long id;
    private Integer type;
    private Integer priority;
    private String street;
    private String nr;
    private String country;
    private String postalcode;
    private String city;
    private String who;
    private String what;
    private String additionalInfo;
    private Long dispatcherId;
    private Long emergencyUnitId;
    private Long reportId;
    private Long logId;
    private Date startTime;
    private Date endTime;
    private String assignedUnitName;

    public OperationDto(Operation op) {
        this.id = op.getId();
        this.type = op.getType().getEmergencyType();
        this.priority = op.getPriority().getEmergencyPriority();
        this.street = op.getAddress().getStreet();
        this.nr = op.getAddress().getNumber();
        this.country = op.getAddress().getCountry();
        this.postalcode = op.getAddress().getPostalCode();
        this.city = op.getAddress().getCity();
        this.who = op.getWho();
        this.what = op.getWhat();
        this.additionalInfo = op.getAdditionalInfo();
        this.dispatcherId = op.getInitializer().getId();
        this.emergencyUnitId = op.getAssignedUnit() != null ? op.getAssignedUnit().getId() : null;
        this.assignedUnitName = op.getAssignedUnit() != null ? op.getAssignedUnit().getUserName() : "";
        this.reportId = op.getReport() != null ? op.getReport().getId() : null;
        this.logId = op.getLog() != null ? op.getLog().getId() : null;
        this.startTime = op.getStartTime();
        this.endTime = op.getEndTime();
    }

    public Operation toOperation(UserRepository userRepository, ReportRepository reportRepository, LogRepository logRepository) {
        OperationType opType;
        OperationPriority opPrio;
        Address address;
        Dispatcher dispatcher;
        EmergencyUnit unit;
        Report report;
        Log log;

        switch (priority) {
            case 0: opPrio = OperationPriority.Low; break;
            case 1: opPrio = OperationPriority.Medium; break;
            default: opPrio = OperationPriority.High; break;
        }

        switch (type) {
            case 0: opType = OperationType.FireEmergency; break;
            case 1: opType = OperationType.MedicalEmergency; break;
            default: opType = OperationType.PoliceEmergency; break;
        }

        address = new Address(street, nr, country, postalcode, city, null);

        dispatcher = userRepository.findDispatcherById(dispatcherId);

        unit = emergencyUnitId != null ? userRepository.findEmergencyUnitById(emergencyUnitId) : null;

        report = reportId != null ? reportRepository.findReportById(reportId) : null;

        log = logId != null ? logRepository.findLogById(logId) : null;

        return new Operation(opType, opPrio, address, who, what, additionalInfo, dispatcher, unit, report, log, startTime, endTime, id);
    }
}
