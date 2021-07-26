package DTO;

import java.io.Serializable;

public class RequestResponse implements Serializable {
    public RequestResponse.Type type;

    public enum Type{
        LoginSuccessful, AlreadyLoggedIn, IncorrectPassword, UsernameNotRegistered, InsufficientTransferBudget, AlreadyBought
    }

    public RequestResponse(RequestResponse.Type type) {
        this.type = type;
    }
}
