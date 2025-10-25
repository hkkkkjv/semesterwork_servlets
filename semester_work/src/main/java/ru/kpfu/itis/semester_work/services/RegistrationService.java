package ru.kpfu.itis.semester_work.services;

import ru.kpfu.itis.semester_work.dao.RegistrationDAO;
import ru.kpfu.itis.semester_work.model.Registration;

import java.util.List;

public class RegistrationService {
    private final RegistrationDAO registrationDAO;

    public RegistrationService(RegistrationDAO registrationDAO) {
        this.registrationDAO = registrationDAO;
    }
    public List<Registration> getUserRegistrations(int userId){
        return registrationDAO.getRegistrationsByUserId(userId);
    }
}
