package engineer.cafisodevs.todoapp.account.services;

import engineer.cafisodevs.todoapp.account.AccountType;
import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.account.dto.AccountDTO;
import engineer.cafisodevs.todoapp.account.repository.AccountRepository;
import engineer.cafisodevs.todoapp.authentication.dto.SignUPRequest;
import engineer.cafisodevs.todoapp.exceptions.AccountNotFoundException;
import engineer.cafisodevs.todoapp.exceptions.EmailAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public EntityUser createAccount(EntityUser accountDTO) {
        if (accountExists(accountDTO.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        EntityUser user = EntityUser.builder()
                .email(accountDTO.getEmail())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .accountType(AccountType.USER)
                .fullName(accountDTO.getFullName())
                .build();

        accountRepository.save(user);

        return user;
    }

    @Override
    public void deleteAccount(String email) {
        if (!accountExists(email)) {
            throw new AccountNotFoundException();
        }

        EntityUser toDelete = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("The account you're trying to delete doesn't exist!"));

        accountRepository.delete(toDelete);
    }

    @Override
    public void deleteAccount(Principal principal) {
        deleteAccount(principal.getName());
    }

    @Override
    public Optional<EntityUser> updateAccount(AccountDTO accountDTO) {
        //TODO: Implement logic, find the best algorithm to know what to update, otherwise just replace the saved user in the database with a new Entity.
        return Optional.empty();
    }

    @Override
    public Optional<EntityUser> loadAccount(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Optional<EntityUser> loadAccount(UUID uuid) {
        return accountRepository.findByUuid(uuid);
    }

    @Override
    public Optional<EntityUser> loadAccount(Principal principal) {
        return loadAccount(principal.getName());
    }

    @Override
    public boolean accountExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public boolean checkPassword(String email, String password) {
        return loadAccount(email).map(user -> passwordEncoder.matches(password, user.getPassword())).orElse(false);
    }

    @Override
    public boolean changePassword(String email, String password) {
        EntityUser user = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("The account you're trying to change the password for doesn't exist!"));

        user.setPassword(passwordEncoder.encode(password));
        accountRepository.save(user);

        return true;
    }

    @Override
    public boolean changePassword(Principal principal, String password) {
        return changePassword(principal.getName(), password);
    }
}
