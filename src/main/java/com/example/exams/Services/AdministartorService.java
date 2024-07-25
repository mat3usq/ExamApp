package com.example.exams.Services;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Repositories.Db.AdministratorsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministartorService {
    @Autowired
    AdministratorsEntityRepository administratorsEntityRepository;

    public AdministartorService(AdministratorsEntityRepository administratorsEntityRepository){
        this.administratorsEntityRepository = administratorsEntityRepository;
    }

    public List<Administrator> getAllAdministrators() {
        return administratorsEntityRepository.findAll();
    }

    public Administrator getAdminById(Integer adminId){
        Optional <Administrator> admin = administratorsEntityRepository.findById(adminId);
        return admin.orElse(null);
    }

    public Administrator getAdminByLogin(String login) {
        return administratorsEntityRepository.findAdministratorByLogin(login);
    }

    public Administrator getAdministratorByEmail(String email) {
        return administratorsEntityRepository.findAdministratorByEmail(email);
    }
    public void editAdministator(Integer administratorId, String firstName, String lastName, String login, String password, String email){
        Administrator administrator = administratorsEntityRepository.findAdministratorById(administratorId);

        if(administrator != null){
            if(firstName != null && !firstName.trim().isEmpty())
                administrator.setFirstname(firstName);

            if(lastName != null && !lastName.trim().isEmpty())
                administrator.setLastname(lastName);

            if(login != null && !login.trim().isEmpty())
                administrator.setLogin(login);

            if(password != null && !password.trim().isEmpty())
                administrator.setPassword(password);

            if(email != null && !email.trim().isEmpty())
                administrator.setEmail(email);

            administratorsEntityRepository.save(administrator);
        }
    }


    public void activate(int id){
        Optional <Administrator> administrator = administratorsEntityRepository.findById(id);
        if(administrator.isPresent()){
            administrator.get().setVerificationStatus(true);
            administratorsEntityRepository.save(administrator.get());
        }
    }

    public void deactivate(int id){
        Optional <Administrator> administrator = administratorsEntityRepository.findById(id);
        if(administrator.isPresent()){
            administrator.get().setVerificationStatus(false);
            administratorsEntityRepository.save(administrator.get());
        }
    }

    public void deleteAdministrator(int id){
        administratorsEntityRepository.deleteById(id);
    }
}
