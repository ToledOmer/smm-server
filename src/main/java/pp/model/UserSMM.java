package pp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Semaphore;


public class UserSMM {
    UUID id;
    String username;
    String password;
    String email;
    Semaphore numOfActions;
    int isConncted;
    String compFile = "";

    @Value("${user.normalActions}")
    int normal;

    @Value("${user.premActions}")
    int premium;

    public UserSMM(@JsonProperty("username") String name,
                   @JsonProperty("password") String password,
                   @JsonProperty("email") String email) {

        id = UUID.randomUUID();
        this.password = password;
        this.username = name;
        this.email = email;
        this.isConncted = 0;
        numOfActions = new Semaphore(normal);
    }

    public String getCompFile() {
        return compFile;
    }

    public void setCompFile(String compFile) {
        this.compFile = compFile;
    }

    public int getNumOfActionsInt() {
        return numOfActions.availablePermits();
    }

    public int getIsConncted() {
        return isConncted;
    }

    public void setIsConncted(int isConncted) {
        this.isConncted = isConncted;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }

    public Semaphore getNumOfActions() {
        return numOfActions;
    }

    public void setPremActions(int numOfnewActions) {
        numOfActions = new Semaphore(premium);
    }

    public void setNumOfActions(Semaphore numOfActions) {
        this.numOfActions = numOfActions;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSMM userSMM = (UserSMM) o;
        return Objects.equals(id, userSMM.id) &&
                Objects.equals(username, userSMM.username) &&
                Objects.equals(email, userSMM.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
