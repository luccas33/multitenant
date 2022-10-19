package luccas33.multitenant.controller;

import luccas33.multitenant.application.StatusException;
import luccas33.multitenant.model.NewTenant;
import luccas33.multitenant.model.ReturnDTO;
import luccas33.multitenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController {

    @Autowired
    private TenantService service;

    @PostMapping("/create")
    public ResponseEntity<ReturnDTO> create(@RequestBody NewTenant newTenant, @RequestHeader String token) {
        try {
            service.createTenant(newTenant, token);
            return getOK();
        } catch (StatusException e) {
            return getError(e);
        }
    }

}
