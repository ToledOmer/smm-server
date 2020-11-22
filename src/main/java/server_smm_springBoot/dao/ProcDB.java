package server_smm_springBoot.dao;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import server_smm_springBoot.model.CompProps;
import server_smm_springBoot.model.UserSMM;

import java.util.*;

@Repository("fakeDB")
public class ProcDB {

    private static List<CompProps> DB = new ArrayList<>();
    private static HashMap<String, UserSMM> DB_USERS = new HashMap<>();

    int InsertTask(CompProps cProp) {


        UUID id = UUID.randomUUID();
        return 1;

    }


    public int insertProc(CompProps cProp) {
        DB.add(cProp);
        return 1;
    }

    public List<CompProps> getAll() {

        return DB;
    }

    public int getSize() {

        return DB.size();

    }

    public boolean EmailExist(String email) {

        return DB_USERS.containsKey(email);

    }

    public void insertUser(UserSMM userSMM) {

        DB_USERS.put(userSMM.getEmail(), userSMM);

    }

    public UserSMM getUser(@NotNull UserSMM userSMM) {
        return DB_USERS.get(userSMM.getEmail());


    }

    public UserSMM getUserByEmail(String userEmail) {
        return DB_USERS.get(userEmail);
    }

    public List<UserSMM> getAllUsers() {
        return new ArrayList<>(DB_USERS.values());
    }

    public UserSMM getUserByUUID(UUID uuid) {
        UserSMM tmp =null;
        for (UserSMM user : DB_USERS.values()) {
            if (user.getId().equals(uuid)) {
                tmp = user;
                break;
            }
        }
        return tmp;

    }
}
