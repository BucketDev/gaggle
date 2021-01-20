package com.bucketdev.gaggle.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Objects;

/**
 * @author rodrigo.loyola
 * @param <Key> type of the identifier
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class CoreEntity<Key> implements Comparable<CoreEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private java.util.Calendar creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Calendar modificationDate;

    private Boolean enabled = true;

    @PrePersist
    public void onCreate() {
        this.setCreationDate(Calendar.getInstance());
    }

    @PreUpdate
    void onPersist() {
        this.setModificationDate(Calendar.getInstance());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoreEntity other = (CoreEntity) obj;
        return Objects.equals(this.id, other.id);
    }
    @Override
    public int compareTo(CoreEntity o) {
        return Math.toIntExact(this.getId().hashCode() - o.getId().hashCode());
    }
}
