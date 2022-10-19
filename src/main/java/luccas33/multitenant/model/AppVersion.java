package luccas33.multitenant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_version", schema = "public")
public class AppVersion {

    @Id
    @Column(name = "id_app_version")
    private Long idAppVersion;

    @Column(name = "version_code")
    private String versionCode;

    @Column(name = "access_url")
    private String accessUrl;

    @Column
    private String createScript;

    @Column(name = "update_script")
    private String updateScript;

    public Long getIdAppVersion() {
        return idAppVersion;
    }

    public void setIdAppVersion(Long idAppVersion) {
        this.idAppVersion = idAppVersion;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getCreateScript() {
        return createScript;
    }

    public void setCreateScript(String createScript) {
        this.createScript = createScript;
    }

    public String getUpdateScript() {
        return updateScript;
    }

    public void setUpdateScript(String updateScript) {
        this.updateScript = updateScript;
    }
}
