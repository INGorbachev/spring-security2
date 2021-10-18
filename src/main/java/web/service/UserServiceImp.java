package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImp implements UserService {

   private UserDao userDao;
   private RoleDao roleDao;

   @Autowired
   public UserServiceImp(UserDao userDao, RoleDao roleDao) {
      this.userDao = userDao;
      this.roleDao = roleDao;
   }

   @Transactional
   @Override
   public void add(User user, Long[] rolesId) {
      HashSet<Role> roles = new HashSet<>();

      for(Long id: rolesId) {
         roles.add(roleDao.getRoleById(id));
      }

      user.setRoles(roles);
      user.setPassword(user.getPassword());
      userDao.add(user);
   }

   @Transactional(readOnly = true)
   @Override
   public List<User> listUsers() {
      return userDao.listUsers();
   }


   @Transactional
   @Override
   public void remove(Long id) {
      userDao.remove(id);
   }

   @Transactional
   @Override
   public void update(User user, Long[] rolesId) {
      Set<Role> roles = new HashSet<>();
      User oldUser = userDao.getUserById(user.getId());

      if (rolesId != null) {
         for (Long id : rolesId) {
            roles.add(roleDao.getRoleById(id));
         }
      } else {
         roles = oldUser.getRoles();
      }

      user.setPassword(oldUser.getPassword());
      user.setRoles(roles);
      userDao.update(user);
   }

   @Transactional(readOnly = true)
   @Override
   public User getUserById(Long id) {
      return userDao.getUserById(id);
   }

   @Transactional(readOnly = true)
   @Override
   public User getUserByFirstName(String name) {
        return userDao.getUserByFirstName(name);
    }

   @Transactional(readOnly = true)
   @Override
   public boolean checkUserById(Long id) {
      return userDao.checkUserById(id);
   }
}
