package onlineexam.blakeexam.service;

import onlineexam.blakeexam.entity.Account;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AccountService {

    int addAccount(Account account);

    boolean updateAccount(Account account);

    boolean updateAvatarImgUrlById(String avatarImgUrl, int id);

    Account getAccountByUsername(String username);

    List<Account> getAccountsByStudentIds(List<Integer> studentIds);

    Map<String, Object> getAccounts(int pageNum, int pageSize);

    Map<String, Object> getAccountsByLevel(int pageNum, int pageSize, int level);

    boolean deleteAccount(int id);

    boolean disabledAccount(int id);

    boolean abledAccount(int id);

    List<Account> getAccountsByIds(Set<Integer> ids);

    Account getAccountById(int id);
}
