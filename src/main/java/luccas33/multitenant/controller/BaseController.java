package luccas33.multitenant.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import luccas33.multitenant.application.StatusException;
import luccas33.multitenant.model.ReturnDTO;
import luccas33.multitenant.model.ReturnDTOImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<ReturnDTO> getOK() {
        return ResponseEntity.ok(new ReturnDTOImp(HttpStatus.OK.value(), "OK"));
    }

    protected ResponseEntity<ReturnDTO> getError(StatusException exception) {
        if (exception.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ReturnDTOImp(exception.getStatus().value(), exception.getMessage()), exception.getStatus());
    }

}
