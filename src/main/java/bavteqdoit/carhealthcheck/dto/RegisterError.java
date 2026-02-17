package bavteqdoit.carhealthcheck.dto;

import lombok.Getter;

@Getter
public enum RegisterError {
    PASSWORD_MISMATCH("register.password.mismatch"),
    EMAIL_EMPTY("register.email.empty"),
    EMAIL_INVALID("register.email.invalid"),
    EMAIL_TAKEN("register.email.taken"),
    PASSWORD_EMPTY("register.password.empty"),
    PASSWORD_TOO_SHORT("register.password.tooShort"),
    USERNAME_EMPTY("register.username.empty"),
    USERNAME_TAKEN("register.username.taken");

    private final String messageKey;

    RegisterError(String messageKey) { this.messageKey = messageKey; }

}
