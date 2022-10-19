package luccas33.multitenant.model;

import luccas33.multitenant.application.StatusException;
import org.springframework.http.HttpStatus;

public class NewTenant {

    private String name;
    private String schema;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void validate() throws StatusException {
        StringBuilder errors = new StringBuilder();
        if (name == null || name.trim().isEmpty()) {
            errors.append("name is required").append("; ");
        }
        if (schema == null || schema.trim().isEmpty()) {
            errors.append("schema is required").append("; ");
        }
        if (version == null || version.trim().isEmpty()) {
            errors.append("version is required");
        }
        if (errors.length() > 0) {
            throw new StatusException(errors.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
