package luccas33.multitenant.model;

import java.util.Date;

public class Token {

    private Long userId;
    private String schemaName;

    public Token() {
    }

    public Token(Long userId, String schemaName) {
        this.userId = userId;
        this.schemaName = schemaName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
