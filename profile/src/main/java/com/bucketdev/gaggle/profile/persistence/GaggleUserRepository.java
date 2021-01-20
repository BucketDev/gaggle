package com.bucketdev.gaggle.profile.persistence;

import com.bucketdev.gaggle.profile.domain.GaggleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rodrigo.loyola
 */
@Repository
public interface GaggleUserRepository extends JpaRepository<GaggleUser, Long> {
}
