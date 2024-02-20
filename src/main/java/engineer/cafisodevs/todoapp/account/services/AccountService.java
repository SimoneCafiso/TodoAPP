package engineer.cafisodevs.todoapp.account.services;

import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.account.dto.AccountDTO;
import engineer.cafisodevs.todoapp.authentication.dto.SignUPRequest;

import java.security.Principal;
import java.util.Optional;

public interface AccountService {
    /**
     * This method creates a new account
     * @param accountDTO
     * @return Optional of EntityUser
     */
    EntityUser createAccount(EntityUser accountDTO);


    /**
     * This method deletes an account
     * @param email
     * @return void
     */
    void deleteAccount(String email);
    /**
     * This method deletes an account
     * @param principal
     * @return void
     */
    void deleteAccount(Principal principal);

    /**
     * This method updates an account
     * @param accountDTO
     * @return Optional of EntityUser
     */
    Optional<EntityUser> updateAccount(AccountDTO accountDTO);
    /**
     * This method updates an account
     * @param principal
     * @return Optional of EntityUser
     */
    Optional<EntityUser> loadAccount(String email);
    /**
     * This method updates an account
     * @param principal
     * @return Optional of EntityUser
     */
    Optional<EntityUser> loadAccount(Principal principal);

    /**
     * This method checks if an account exists
     * @param email
     * @return boolean
     */
    boolean accountExists(String email);
    /**
     * This method checks if an account exists
     * @param principal
     * @return boolean
     */

    boolean checkPassword(String email, String password);
    /**
     * This method checks if an account exists
     * @param principal
     * @return boolean
     */

    boolean changePassword(String email, String password);

    /**
     * This method checks if an account exists
     * @param principal
     * @return boolean
     */
    boolean changePassword(Principal principal, String password);

}
