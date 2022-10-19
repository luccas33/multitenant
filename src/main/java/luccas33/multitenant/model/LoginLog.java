package luccas33.multitenant.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @Column(name = "id_login_log")
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "login_log_seq")
    private Long idLoginLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "logged_at")
    private Date loggedAt;

    public Long getIdLoginLog() {
        return idLoginLog;
    }

    public void setIdLoginLog(Long idLoginLog) {
        this.idLoginLog = idLoginLog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(Date loggedAt) {
        this.loggedAt = loggedAt;
    }
}
