package com.example.profile.api;

import com.example.profile.exception.*;
import com.example.profile.model.Address;
import com.example.profile.model.Profile;
import com.example.profile.repository.AddressRepository;
import com.example.profile.repository.ProfileRepository;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * API for Profile and address
 *
 * @since 23/2/19.
 */
@SwaggerDefinition(
        info = @Info(
                description = "Profile API for Customer",
                version = "V1.0.0",
                title = "Profile API",
                contact = @Contact(
                        name = "Johanes Soetanto",
                        email = "otnateos@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}
)
@RestController
@RequestMapping("/profile")
public class ProfileController
{
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostMapping
    @ApiOperation(value = "Create profile for a customer", response = Profile.class)
    @ApiResponse(code = 400, message = "Existing profile exist for user")
    public Profile createProfile(@ApiParam(value = "profile record need to be created", required = true)
                                     @Valid @RequestBody Profile profile)
            throws ExistingProfileExistException
    {

        log.info("Creating profile for User Id: {}", profile.getUserId());
        if (profileRepository.existsById(profile.getUserId())) {
            throw new ExistingProfileExistException();
        }

        profileRepository.save(profile);
        log.info("Created profile for User Id: {}", profile.getUserId());
        return profile;
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get customer profile", response = Profile.class)
    @ApiResponse(code = 404, message = "Profile does not exist for user")
    public Profile getProfile(@ApiParam(value = "customer id to find the profile", required = true) @PathVariable("userId") String userId)
            throws ProfileNotFoundException
    {

        log.info("Finding profile for User Id: {}", userId);
        Optional<Profile> profile = profileRepository.findById(userId);
        return profile.orElseThrow(ProfileNotFoundException::new);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update customer profile", code = 204)
    @ApiResponse(code = 400, message = "Profile not available for user")
    public void updateProfile(@ApiParam(value = "customer id to update the profile", required = true) @PathVariable("userId") String userId,
                              @ApiParam(value = "updated profile record", required = true) @Valid @RequestBody Profile profile)
            throws ProfileNotAvailableException {

        log.info("Updating profile for User Id: {}", userId);
        if (!profileRepository.existsById(userId)) {
            throw new ProfileNotAvailableException();
        }

        profile.setUserId(userId);
        profileRepository.save(profile);
        log.info("Updated profile for User Id: {}", userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete customer profile", code = 204)
    @ApiResponse(code = 400, message = "Profile not available for user")
    public void deleteProfile(@ApiParam(value = "customer id to delete the profile", required = true) @PathVariable("userId") String userId)
            throws ProfileNotAvailableException
    {

        log.info("Deleting profile for User Id: {}", userId);
        if (!profileRepository.existsById(userId)) {
            throw new ProfileNotAvailableException();
        }
        int deletedAddresses = addressRepository.deleteAddressesByUser(userId);
        log.info("Deleted {} addresses for User Id: {}", deletedAddresses, userId);

        profileRepository.deleteById(userId);
        log.info("Profile for User Id: {} has been deleted", userId);
    }

    @PostMapping("/{userId}/address")
    @ApiOperation(value = "Add address detail to customer profile", response = Address.class)
    @ApiResponse(code = 400, message = "Profile not available for user")
    public Address createAddress(@ApiParam(value = "customer id to add address detail", required = true) @PathVariable("userId") String userId,
                                 @ApiParam(value = "new address detail for customer", required = true) @Valid @RequestBody Address address)
            throws ProfileNotAvailableException
    {

        log.info("Creating address for User Id: {}", userId);
        if (!profileRepository.existsById(userId)) {
            throw new ProfileNotAvailableException();
        }
        address.setId(null);
        address.setProfile(profileRepository.findById(userId).get());
        addressRepository.save(address);
        log.info("Created address {} for User Id: {}", address.getId(), userId);
        return address;
    }

    @GetMapping("/{userId}/addresses")
    @ApiOperation(value = "Get addresses of customer", response = Address.class, responseContainer = "List")
    @ApiResponse(code = 400, message = "Profile not available for user")
    public List<Address> getUserAddresses(@ApiParam(value = "customer id to get address details", required = true) @PathVariable("userId") String userId)
            throws ProfileNotAvailableException {

        log.info("Finding addresses for User Id: {}", userId);
        if (!profileRepository.existsById(userId)) {
            throw new ProfileNotAvailableException();
        }
        List<Address> addresses = addressRepository.findAddressesByUser(userId);
        log.info("Found {} addresses for User Id: {}", addresses.size(), userId);
        return addresses;
    }

    @GetMapping("/address/{addressId}")
    @ApiOperation(value = "Get an address of customer", response = Address.class)
    @ApiResponse(code = 404, message = "Address not found")
    public Address getAddress(@ApiParam(value = "address id to retrieve", required = true) @PathVariable("addressId") Long addressId)
            throws AddressNotFoundException {

        log.info("Finding address by Id: {}", addressId);
        Optional<Address> address = addressRepository.findById(addressId);
        return address.orElseThrow(AddressNotFoundException::new);
    }

    @PutMapping("/address/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update customer address", code = 204)
    @ApiResponse(code = 400, message = "Address not available")
    public void updateAddress(@ApiParam(value = "address id to update", required = true) @PathVariable("addressId") Long addressId,
                              @ApiParam(value = "address detail to update", required = true) @Valid @RequestBody Address address)
            throws AddressNotAvailableException {

        log.info("Updating address Id: {}", addressId);
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotAvailableException();
        }

        address.setId(addressId);
        addressRepository.save(address);
        log.info("Updated address Id: {}", addressId);
    }

    @DeleteMapping("/address/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete customer address", code = 204)
    @ApiResponse(code = 400, message = "Address not available")
    public void deleteAddress(@ApiParam(value = "address id to delete", required = true) @PathVariable("addressId") Long addressId)
            throws AddressNotAvailableException {

        log.info("Deleting address Id: {}", addressId);
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotAvailableException();
        }
        addressRepository.deleteById(addressId);
        log.info("Deleted address Id: {}", addressId);
    }
}
