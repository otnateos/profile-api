package com.example.profile.repository;

import com.example.profile.model.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Repository for address.
 *
 * @since 23/2/19.
 */
public interface AddressRepository extends CrudRepository<Address, Long>
{
    @Query("SELECT address FROM Address address WHERE LOWER(address.profile) = LOWER(:userId)")
    List<Address> findAddressesByUser(String userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Address address WHERE LOWER(address.profile) = LOWER(:userId)")
    int deleteAddressesByUser(@Param("userId")String userId);
}
