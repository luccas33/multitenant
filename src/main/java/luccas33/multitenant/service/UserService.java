package luccas33.multitenant.service;

import luccas33.multitenant.application.*;
import luccas33.multitenant.model.*;
import luccas33.multitenant.repository.LoginLogRepository;
import luccas33.multitenant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private TenantService tenantService;

    public void saveNewUser(NewUser newUser) throws StatusException {
        newUser.validate();
        User user = new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(generatePassword(newUser.getEmail(), newUser.getPassword()));
        user.setAdm(false);

        Tenant tenant = tenantService.getTenant(newUser.getTenantName());
        if (userRepository.findByEmailAndTenant(newUser.getEmail(), tenant) != null) {
            throw new StatusException("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        user.setTenant(tenant);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw logAndThrow("Error saving new user", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public LoggedUser login(Login login) throws StatusException {
        login.validate();
        Tenant tenant = tenantService.getTenant(login.getTenantName());
        if (tenant == null) {
            throw new StatusException("Tenant not found", HttpStatus.NOT_ACCEPTABLE);
        }
        String password = generatePassword(login.getEmail(), login.getPassword());
        User user = userRepository.findByEmailAndPasswordAndTenant(login.getEmail(), password, tenant);
        if (user == null) {
            throw new StatusException("Incorrect email or password", HttpStatus.UNAUTHORIZED);
        }

        TenantIdentifier.setCurrentTenant(tenant.getSchemaName());
        LoginLog log = new LoginLog();
        log.setUser(user);
        log.setLoggedAt(new Date());
        try {
            loginLogRepository.save(log);
        } catch (Exception e) {
            throw logAndThrow("Error saving new login", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }

        LoggedUser logged = new LoggedUser();
        logged.setUserName(user.getName());
        logged.setEmail(user.getEmail());
        logged.setTenantName(tenant.getName());
        Token token = new Token(user.getIdUser(), tenant.getSchemaName());
        logged.setToken(TokenUtil.generateToken(token));
        logged.setExpiresInMinutes(TokenUtil.TOKEN_VALIDITY_MIN);
        logged.setAccessUrl(tenant.getAppVersion().getAccessUrl());

        TokenUtil.readToken(logged.getToken());

        return logged;
    }

    private String generatePassword(String email, String pass) {
        return Utils.md5(email + "_user-pass_" + pass);
    }

}
