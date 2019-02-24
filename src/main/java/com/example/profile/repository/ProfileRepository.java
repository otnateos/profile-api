package com.example.profile.repository;

import com.example.profile.model.Profile;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for profile.
 *
 * @since 23/2/19.
 */
public interface ProfileRepository extends CrudRepository<Profile, String>
{
}
