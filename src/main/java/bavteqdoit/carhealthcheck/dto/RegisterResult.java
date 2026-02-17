package bavteqdoit.carhealthcheck.dto;

public class RegisterResult {
    private final boolean success;
    private final RegisterError error;

    private RegisterResult(boolean success, RegisterError error) {
        this.success = success;
        this.error = error;
    }

    public static RegisterResult ok() { return new RegisterResult(true, null); }
    public static RegisterResult fail(RegisterError error) { return new RegisterResult(false, error); }

    public boolean isSuccess() { return success; }
    public RegisterError getError() { return error; }
}