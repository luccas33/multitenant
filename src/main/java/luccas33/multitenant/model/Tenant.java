package luccas33.multitenant.model;

import javax.persistence.*;

@Entity
@Table(name = "tenant", schema = "public")
public class Tenant {

    @Id
    @Column(name = "id_tenant")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "public.tenant_seq")
    private Long idTenant;

    @Column(name = "name")
    private String name;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "updated")
    private Boolean updated;

    @Column(name = "created")
    private Boolean created;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_app_version")
    private AppVersion appVersion;

    public Long getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Long idTenant) {
        this.idTenant = idTenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }
}
