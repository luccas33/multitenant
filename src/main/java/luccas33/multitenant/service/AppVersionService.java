package luccas33.multitenant.service;

import luccas33.multitenant.application.StatusException;
import luccas33.multitenant.model.AppVersion;
import luccas33.multitenant.repository.AppVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AppVersionService extends BaseService {

    @Autowired
    private AppVersionRepository appVersionRepository;

    public AppVersion getAppVersion(String versionCode) throws StatusException {
        AppVersion appVersion;
        try {
            appVersion = appVersionRepository.findByVersionCode(versionCode);
        } catch (Exception e) {
            throw logAndThrow("Error finding version by code", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (appVersion == null) {
            throw new StatusException("Version not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return appVersion;
    }

}
