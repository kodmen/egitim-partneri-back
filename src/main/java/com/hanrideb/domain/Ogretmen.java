package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Ogretmen.
 */
@Entity
@Table(name = "ogretmen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ogretmen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "aciklama")
    private String aciklama;

    @OneToOne
    @JoinColumn(unique = true)
    private User ogretmenUser;

    @OneToMany(mappedBy = "dersOgretmeni")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dersMufredat", "dersForm", "kayitlar1s", "dersOgretmeni" }, allowSetters = true)
    private Set<Ders> derslers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ogretmen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Ogretmen aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public User getOgretmenUser() {
        return this.ogretmenUser;
    }

    public void setOgretmenUser(User user) {
        this.ogretmenUser = user;
    }

    public Ogretmen ogretmenUser(User user) {
        this.setOgretmenUser(user);
        return this;
    }

    public Set<Ders> getDerslers() {
        return this.derslers;
    }

    public void setDerslers(Set<Ders> ders) {
        if (this.derslers != null) {
            this.derslers.forEach(i -> i.setDersOgretmeni(null));
        }
        if (ders != null) {
            ders.forEach(i -> i.setDersOgretmeni(this));
        }
        this.derslers = ders;
    }

    public Ogretmen derslers(Set<Ders> ders) {
        this.setDerslers(ders);
        return this;
    }

    public Ogretmen addDersler(Ders ders) {
        this.derslers.add(ders);
        ders.setDersOgretmeni(this);
        return this;
    }

    public Ogretmen removeDersler(Ders ders) {
        this.derslers.remove(ders);
        ders.setDersOgretmeni(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ogretmen)) {
            return false;
        }
        return id != null && id.equals(((Ogretmen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ogretmen{" +
            "id=" + getId() +
            ", aciklama='" + getAciklama() + "'" +
            "}";
    }
}
