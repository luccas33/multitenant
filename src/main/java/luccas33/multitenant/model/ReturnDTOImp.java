package luccas33.multitenant.model;

public class ReturnDTOImp implements ReturnDTO {

    private int status;
    private String statusMessage;

    public ReturnDTOImp() {
    }

    public ReturnDTOImp(int status, String statusMessage) {
        this.status = status;
        this.statusMessage = statusMessage;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
