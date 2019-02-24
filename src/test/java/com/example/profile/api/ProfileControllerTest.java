package com.example.profile.api;

import com.example.profile.model.Address;
import com.example.profile.model.Profile;
import com.example.profile.repository.AddressRepository;
import com.example.profile.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @since 23/2/19.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTest
{
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AddressRepository addressRepository;
    @MockBean
    private ProfileRepository profileRepository;

    private Profile profile;
    private Profile profile2;

    private Address address;

    @Before
    public void setUp()
    {
        this.profile = profile("1", "John", "Smith", "29-02-2000");
        this.profile2 = profile("2", "John", "Doe", "01-02-1983");

        this.address = new Address();
        address.setId(1L);
        address.setProfile(profile);
        address.setLine1("George St");

        given(profileRepository.findById("1")).willReturn(Optional.of(profile));
        given(profileRepository.findById("2")).willReturn(Optional.empty());
        given(profileRepository.existsById("1")).willReturn(true);
        given(profileRepository.existsById("2")).willReturn(false);

        given(addressRepository.findById(1L)).willReturn(Optional.of(address));
        given(addressRepository.findById(2L)).willReturn(Optional.empty());
        given(addressRepository.existsById(1L)).willReturn(true);
        given(addressRepository.existsById(2L)).willReturn(false);
        given(addressRepository.findAddressesByUser("1")).willReturn(Arrays.asList(address));
    }

    private Profile profile(String id, String firstName, String lastName, String dob)
    {
        Profile profile = new Profile();
        profile.setUserId(id);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setDateOfBirth(dob);
        return profile;
    }

    @Test
    public void testCreateProfile_existingProfileFound()
            throws Exception
    {
        mvc.perform(post("/profile", profile))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProfile()
            throws Exception
    {
        mvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(profile2)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void testGetProfile_notFound()
            throws Exception
    {
        mvc.perform(get("/profile/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProfile_found()
            throws Exception
    {
        mvc.perform(get("/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    public void testUpdateProfile_notFound()
            throws Exception
    {
        mvc.perform(put("/profile/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(profile2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProfile()
            throws Exception
    {
        mvc.perform(put("/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(profile)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProfile_notFound()
            throws Exception
    {
        mvc.perform(delete("/profile/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProfile()
            throws Exception
    {
        mvc.perform(delete("/profile/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateAddress_profileNotFound()
            throws Exception
    {
        mvc.perform(post("/profile/2/address", address))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAddress()
            throws Exception
    {
        mvc.perform(post("/profile/1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.line1", is("George St")));
    }

    @Test
    public void testGetAddress()
            throws Exception
    {
        mvc.perform(get("/profile/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.line1", is("George St")));
    }

    @Test
    public void testGetAddress_notFound()
            throws Exception
    {
        mvc.perform(get("/profile/address/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserAddresses_profileNotFound()
            throws Exception
    {
        mvc.perform(get("/profile/2/addresses"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserAddresses()
            throws Exception
    {
        mvc.perform(get("/profile/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testUpdateAddress_addressNotFound()
            throws Exception
    {
        mvc.perform(put("/profile/address/2", address))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAddress()
            throws Exception
    {
        mvc.perform(put("/profile/address/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(address)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAddress()
            throws Exception
    {
        mvc.perform(delete("/profile/address/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAddress_addressNotFound()
            throws Exception
    {
        mvc.perform(delete("/profile/address/2"))
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}