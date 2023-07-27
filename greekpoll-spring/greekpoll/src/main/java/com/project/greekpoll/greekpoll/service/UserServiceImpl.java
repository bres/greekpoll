package com.project.greekpoll.greekpoll.service;


import com.project.greekpoll.greekpoll.entity.Role;
import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.repository.RoleRepository;
import com.project.greekpoll.greekpoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository ) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
   // ακροατης που δημιουργει την 1η μονο φορα χρηστη στην ΒΔ
    @EventListener(ApplicationReadyEvent.class)
    public User createAdmin() {
        if (userRepository.findByUsername("admin") == null) {


            User admin = new User("-", "admin", "-","-", passwordEncoder.encode("Admin!1234"),"admin" ,
                    roleRepository.findByName("Admin"));
            return userRepository.save(admin);
        }

        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User authenticatedUser = userRepository.findByUsername(username);
        //this.authenticatedUser = authenticatedUser;
        if (authenticatedUser == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(authenticatedUser.getUsername(), authenticatedUser.getPassword(), getAuthorities(authenticatedUser.getRoleByUserrole()));
    }


    public Collection<? extends GrantedAuthority> getAuthorities(Role role) {

        return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
    }


    @Override
    public User save(User user) {

        String gender = user.getGender().trim();
        String fullname = user.getFullname().trim();
        String prefecture = user.getPrefecture().trim();
        String municipality = user.getMunicipality().trim();
        String username = user.getUsername().trim();
        String password = passwordEncoder.encode(user.getPassword());
        Role userRole = userRoleService.findById(user.getRoleByUserrole().getId());


        if (userRepository.findByUsername(username) == null) {

            User newUser = new User(gender, fullname, prefecture, municipality, password, username,
                    userRole);
            return userRepository.save(newUser);

        } else return null;



    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }




    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void edit(int id, User user , String newPassword) {
        User found = userRepository.findById(id);

        found.setGender(user.getGender());
        found.setFullname(user.getFullname());
        found.setPrefecture(user.getPrefecture());
        found.setMunicipality(user.getMunicipality());


        if (   newPassword !=""){

            found.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(found);
    }




    @Override
    public void changepass(int id, User user) {
        User found = userRepository.findById(id);
        found.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(found);
    }



    @Override
    public void disable(User user) {

        user.setRoleByUserrole(roleRepository.findByName("Pending"));
        userRepository.save(user);
    }

    @Override
    public void enable(User user) {
        user.setRoleByUserrole(roleRepository.findByName("Enrolled"));
        userRepository.save(user);
    }



    // μεθοδος που επιστρεφει τυχαια συμβολοσειρα
    public static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
