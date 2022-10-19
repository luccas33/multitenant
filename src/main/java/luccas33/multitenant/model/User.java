package luccas33.multitenant.model;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "public.user_seq")
    private Long idUser;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "adm")
    private Boolean adm;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tenant")
    private Tenant tenant;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Boolean getAdm() {
        return adm;
    }

    public void setAdm(Boolean adm) {
        this.adm = adm;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
