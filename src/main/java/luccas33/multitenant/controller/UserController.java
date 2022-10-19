package luccas33.multitenant.controller;

import luccas33.multitenant.application.StatusException;
import luccas33.multitenant.model.LoggedUser;
import luccas33.multitenant.model.Login;
import luccas33.multitenant.model.NewUser;
import luccas33.multitenant.model.ReturnDTO;
import luccas33.multitenant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<ReturnDTO> register(@RequestBody NewUser body) {
        try {
            service.saveNewUser(body);
            return getOK();
        } catch (StatusException e) {
            return getError(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ReturnDTO> login(@RequestBody Login body) {
        try {
            LoggedUser logged = service.login(body);
            return ResponseEntity.ok(logged);
        } catch (StatusException e) {
            return getError(e);
        }
    }

}
