package serverapi.Security;


public class AccountVerification {
    private Boolean isVerified;

    public AccountVerification(Boolean isVerified){
        this.isVerified = isVerified;
    }

    public Boolean cheking(){
        if(isVerified.equals(true)){
            return true;
        } else{
            return false;
        }
    }
}
