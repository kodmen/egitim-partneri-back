package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SoruKazanimlari.
 */
@Entity
@Table(name = "soru_kazanimlari")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoruKazanimlari implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "kazanim")
    private String kazanim;

    @ManyToMany(mappedBy = "kazanimlars")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kazanimlars", "aitOldTestlers" }, allowSetters = true)
    private Set<Soru> aitOldSorulars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SoruKazanimlari id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKazanim() {
        return this.kazanim;
    }

    public SoruKazanimlari kazanim(String kazanim) {
        this.setKazanim(kazanim);
        return this;
    }

    public void setKazanim(String kazanim) {
        this.kazanim = kazanim;
    }

    public Set<Soru> getAitOldSorulars() {
        return this.aitOldSorulars;
    }

    public void setAitOldSorulars(Set<Soru> sorus) {
        if (this.aitOldSorulars != null) {
            this.aitOldSorulars.forEach(i -> i.removeKazanimlar(this));
        }
        if (sorus != null) {
            sorus.forEach(i -> i.addKazanimlar(this));
        }
        this.aitOldSorulars = sorus;
    }

    public SoruKazanimlari aitOldSorulars(Set<Soru> sorus) {
        this.setAitOldSorulars(sorus);
        return this;
    }

    public SoruKazanimlari addAitOldSorular(Soru soru) {
        this.aitOldSorulars.add(soru);
        soru.getKazanimlars().add(this);
        return this;
    }

    public SoruKazanimlari removeAitOldSorular(Soru soru) {
        this.aitOldSorulars.remove(soru);
        soru.getKazanimlars().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoruKazanimlari)) {
            return false;
        }
        return id != null && id.equals(((SoruKazanimlari) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoruKazanimlari{" +
            "id=" + getId() +
            ", kazanim='" + getKazanim() + "'" +
            "}";
    }
}
