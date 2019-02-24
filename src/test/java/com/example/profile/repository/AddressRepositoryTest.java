package com.example.profile.repository;

import com.example.profile.ProfileApplication;
import com.example.profile.model.Address;
import com.example.profile.model.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @since 23/2/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProfileApplication.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
public class AddressRepositoryTest
{
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Profile user1;
    private Profile user2;
    private Address address1user1;
    private Address address2user1;
    private Address address1user2;

    @Before
    public void setUp()
    {
        this.user1 = profile(UUID.randomUUID().toString(), "George", "Smith");
        this.user2 = profile(UUID.randomUUID().toString(), "John", "Smith");
        profileRepository.save(user1);
        profileRepository.save(user2);

        this.address1user1 = address("George St", user1);
        this.address2user1 = address("Pitt St", user1);
        this.address1user2 = address("Elizabeth St", user2);
    }

    private Profile profile(String id, String firstName, String lastName)
    {
        Profile profile = new Profile();
        profile.setUserId(id);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        return profile;
    }

    private Address address(String line1, Profile profile)
    {
        Address address = new Address();
        address.setProfile(profile);
        address.setLine1(line1);
        return address;
    }

    @Test
    public void testFindAddressesByUser()
    {
        int initSize = addressRepository.findAddressesByUser(user1.getUserId()).size();
        addressRepository.save(address1user1);
        addressRepository.save(address2user1);
        addressRepository.save(address1user2);
        assertEquals(addressRepository.findAddressesByUser(user1.getUserId()).size(), initSize + 2);
    }

    @Test
    public void testDeleteAddressesByUser()
            throws Exception
    {
        addressRepository.save(address1user1);
        addressRepository.save(address2user1);
        addressRepository.save(address1user2);
        assertEquals(2, addressRepository.deleteAddressesByUser(user1.getUserId()));

    }
}