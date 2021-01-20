package com.bucketdev.gaggle.profile.domain;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author rodrigo.loyola
 */
@Entity
@Table(name = "gaggle_user")
@Getter @Setter
public class GaggleUser extends CoreEntity<Long> {

    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    private String photoURL;

}
