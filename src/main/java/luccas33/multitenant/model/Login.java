package luccas33.multitenant.model;

import luccas33.multitenant.application.StatusException;
import org.springframework.http.HttpStatus;

public class Login {

    private String email;
    private String password;
    private String tenantName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void validate() throws StatusException {
        StringBuilder errors = new StringBuilder();
        if (email == null || email.trim().isEmpty()) {
            errors.append("email is required").append("; ");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.append("password is required").append("; ");
        }
        if (tenantName == null || tenantName.trim().isEmpty()) {
            errors.append("tenantName is required");
        }
        if (errors.length() > 0) {
            throw new StatusException(errors.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
