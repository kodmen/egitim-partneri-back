package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Soru.
 */
@Entity
@Table(name = "soru")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Soru implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cevap")
    private String cevap;

    @ManyToMany
    @JoinTable(
        name = "rel_soru__kazanimlar",
        joinColumns = @JoinColumn(name = "soru_id"),
        inverseJoinColumns = @JoinColumn(name = "kazanimlar_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aitOldSorulars" }, allowSetters = true)
    private Set<SoruKazanimlari> kazanimlars = new HashSet<>();

    @ManyToMany(mappedBy = "sorulars")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sorulars", "testBolum" }, allowSetters = true)
    private Set<SoruTest> aitOldTestlers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Soru id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCevap() {
        return this.cevap;
    }

    public Soru cevap(String cevap) {
        this.setCevap(cevap);
        return this;
    }

    public void setCevap(String cevap) {
        this.cevap = cevap;
    }

    public Set<SoruKazanimlari> getKazanimlars() {
        return this.kazanimlars;
    }

    public void setKazanimlars(Set<SoruKazanimlari> soruKazanimlaris) {
        this.kazanimlars = soruKazanimlaris;
    }

    public Soru kazanimlars(Set<SoruKazanimlari> soruKazanimlaris) {
        this.setKazanimlars(soruKazanimlaris);
        return this;
    }

    public Soru addKazanimlar(SoruKazanimlari soruKazanimlari) {
        this.kazanimlars.add(soruKazanimlari);
        soruKazanimlari.getAitOldSorulars().add(this);
        return this;
    }

    public Soru removeKazanimlar(SoruKazanimlari soruKazanimlari) {
        this.kazanimlars.remove(soruKazanimlari);
        soruKazanimlari.getAitOldSorulars().remove(this);
        return this;
    }

    public Set<SoruTest> getAitOldTestlers() {
        return this.aitOldTestlers;
    }

    public void setAitOldTestlers(Set<SoruTest> soruTests) {
        if (this.aitOldTestlers != null) {
            this.aitOldTestlers.forEach(i -> i.removeSorular(this));
        }
        if (soruTests != null) {
            soruTests.forEach(i -> i.addSorular(this));
        }
        this.aitOldTestlers = soruTests;
    }

    public Soru aitOldTestlers(Set<SoruTest> soruTests) {
        this.setAitOldTestlers(soruTests);
        return this;
    }

    public Soru addAitOldTestler(SoruTest soruTest) {
        this.aitOldTestlers.add(soruTest);
        soruTest.getSorulars().add(this);
        return this;
    }

    public Soru removeAitOldTestler(SoruTest soruTest) {
        this.aitOldTestlers.remove(soruTest);
        soruTest.getSorulars().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Soru)) {
            return false;
        }
        return id != null && id.equals(((Soru) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Soru{" +
            "id=" + getId() +
            ", cevap='" + getCevap() + "'" +
            "}";
    }
}
