package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mufredat.
 */
@Entity
@Table(name = "mufredat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mufredat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "mufredat_baslik", length = 500)
    private String mufredatBaslik;

    @Column(name = "toplam_sure")
    private String toplamSure;

    @Column(name = "bolum_sayi")
    private Integer bolumSayi;

    @ManyToMany
    @JoinTable(
        name = "rel_mufredat__bolumler",
        joinColumns = @JoinColumn(name = "mufredat_id"),
        inverseJoinColumns = @JoinColumn(name = "bolumler_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "analizBolums", "testlers", "mufredatlars" }, allowSetters = true)
    private Set<Bolum> bolumlers = new HashSet<>();

    @JsonIgnoreProperties(value = { "dersMufredat", "dersForm", "kayitlar1s", "dersOgretmeni" }, allowSetters = true)
    @OneToOne(mappedBy = "dersMufredat")
    private Ders mufredatDers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mufredat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMufredatBaslik() {
        return this.mufredatBaslik;
    }

    public Mufredat mufredatBaslik(String mufredatBaslik) {
        this.setMufredatBaslik(mufredatBaslik);
        return this;
    }

    public void setMufredatBaslik(String mufredatBaslik) {
        this.mufredatBaslik = mufredatBaslik;
    }

    public String getToplamSure() {
        return this.toplamSure;
    }

    public Mufredat toplamSure(String toplamSure) {
        this.setToplamSure(toplamSure);
        return this;
    }

    public void setToplamSure(String toplamSure) {
        this.toplamSure = toplamSure;
    }

    public Integer getBolumSayi() {
        return this.bolumSayi;
    }

    public Mufredat bolumSayi(Integer bolumSayi) {
        this.setBolumSayi(bolumSayi);
        return this;
    }

    public void setBolumSayi(Integer bolumSayi) {
        this.bolumSayi = bolumSayi;
    }

    public Set<Bolum> getBolumlers() {
        return this.bolumlers;
    }

    public void setBolumlers(Set<Bolum> bolums) {
        this.bolumlers = bolums;
    }

    public Mufredat bolumlers(Set<Bolum> bolums) {
        this.setBolumlers(bolums);
        return this;
    }

    public Mufredat addBolumler(Bolum bolum) {
        this.bolumlers.add(bolum);
        bolum.getMufredatlars().add(this);
        return this;
    }

    public Mufredat removeBolumler(Bolum bolum) {
        this.bolumlers.remove(bolum);
        bolum.getMufredatlars().remove(this);
        return this;
    }

    public Ders getMufredatDers() {
        return this.mufredatDers;
    }

    public void setMufredatDers(Ders ders) {
        if (this.mufredatDers != null) {
            this.mufredatDers.setDersMufredat(null);
        }
        if (ders != null) {
            ders.setDersMufredat(this);
        }
        this.mufredatDers = ders;
    }

    public Mufredat mufredatDers(Ders ders) {
        this.setMufredatDers(ders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mufredat)) {
            return false;
        }
        return id != null && id.equals(((Mufredat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mufredat{" +
            "id=" + getId() +
            ", mufredatBaslik='" + getMufredatBaslik() + "'" +
            ", toplamSure='" + getToplamSure() + "'" +
            ", bolumSayi=" + getBolumSayi() +
            "}";
    }
}
