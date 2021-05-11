package com.example.hoxify;

import com.example.hoxify.user.User;
import com.example.hoxify.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUsername_whenUserExists_returnUser(){
        User user = new User();

        user.setUsername("test-user");
        user.setDisplayName("test-user");
        user.setPassword("P4ssword");

        testEntityManager.persist(user);

        User inDb = userRepository.findByUsername("test-user");
        assertThat(inDb).isNotNull();
    }

    @Test
    public void findByUsername_whenUserDoesNotExist_returnsNull(){
        User inDb = userRepository.findByUsername("non-existing-user");
        assertThat(inDb).isNull();
    }
}
