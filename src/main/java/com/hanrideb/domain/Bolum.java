package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Bolum.
 */
@Entity
@Table(name = "bolum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bolum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "bolum_baslik", length = 500)
    private String bolumBaslik;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "dokuman")
    private String dokuman;

    @Column(name = "puan")
    private Integer puan;

    @Size(max = 1000)
    @Column(name = "video_link", length = 1000)
    private String videoLink;

    @Column(name = "sure")
    private String sure;

    @Column(name = "sira")
    private Integer sira;

    @OneToMany(mappedBy = "aitOldBolum")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aitOldBolum", "kayitlar3s" }, allowSetters = true)
    private Set<DersAnaliz> analizBolums = new HashSet<>();

    @OneToMany(mappedBy = "testBolum")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sorulars", "testBolum" }, allowSetters = true)
    private Set<SoruTest> testlers = new HashSet<>();

    @ManyToMany(mappedBy = "bolumlers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bolumlers", "mufredatDers" }, allowSetters = true)
    private Set<Mufredat> mufredatlars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bolum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBolumBaslik() {
        return this.bolumBaslik;
    }

    public Bolum bolumBaslik(String bolumBaslik) {
        this.setBolumBaslik(bolumBaslik);
        return this;
    }

    public void setBolumBaslik(String bolumBaslik) {
        this.bolumBaslik = bolumBaslik;
    }

    public String getDokuman() {
        return this.dokuman;
    }

    public Bolum dokuman(String dokuman) {
        this.setDokuman(dokuman);
        return this;
    }

    public void setDokuman(String dokuman) {
        this.dokuman = dokuman;
    }

    public Integer getPuan() {
        return this.puan;
    }

    public Bolum puan(Integer puan) {
        this.setPuan(puan);
        return this;
    }

    public void setPuan(Integer puan) {
        this.puan = puan;
    }

    public String getVideoLink() {
        return this.videoLink;
    }

    public Bolum videoLink(String videoLink) {
        this.setVideoLink(videoLink);
        return this;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSure() {
        return this.sure;
    }

    public Bolum sure(String sure) {
        this.setSure(sure);
        return this;
    }

    public void setSure(String sure) {
        this.sure = sure;
    }

    public Integer getSira() {
        return this.sira;
    }

    public Bolum sira(Integer sira) {
        this.setSira(sira);
        return this;
    }

    public void setSira(Integer sira) {
        this.sira = sira;
    }

    public Set<DersAnaliz> getAnalizBolums() {
        return this.analizBolums;
    }

    public void setAnalizBolums(Set<DersAnaliz> dersAnalizs) {
        if (this.analizBolums != null) {
            this.analizBolums.forEach(i -> i.setAitOldBolum(null));
        }
        if (dersAnalizs != null) {
            dersAnalizs.forEach(i -> i.setAitOldBolum(this));
        }
        this.analizBolums = dersAnalizs;
    }

    public Bolum analizBolums(Set<DersAnaliz> dersAnalizs) {
        this.setAnalizBolums(dersAnalizs);
        return this;
    }

    public Bolum addAnalizBolum(DersAnaliz dersAnaliz) {
        this.analizBolums.add(dersAnaliz);
        dersAnaliz.setAitOldBolum(this);
        return this;
    }

    public Bolum removeAnalizBolum(DersAnaliz dersAnaliz) {
        this.analizBolums.remove(dersAnaliz);
        dersAnaliz.setAitOldBolum(null);
        return this;
    }

    public Set<SoruTest> getTestlers() {
        return this.testlers;
    }

    public void setTestlers(Set<SoruTest> soruTests) {
        if (this.testlers != null) {
            this.testlers.forEach(i -> i.setTestBolum(null));
        }
        if (soruTests != null) {
            soruTests.forEach(i -> i.setTestBolum(this));
        }
        this.testlers = soruTests;
    }

    public Bolum testlers(Set<SoruTest> soruTests) {
        this.setTestlers(soruTests);
        return this;
    }

    public Bolum addTestler(SoruTest soruTest) {
        this.testlers.add(soruTest);
        soruTest.setTestBolum(this);
        return this;
    }

    public Bolum removeTestler(SoruTest soruTest) {
        this.testlers.remove(soruTest);
        soruTest.setTestBolum(null);
        return this;
    }

    public Set<Mufredat> getMufredatlars() {
        return this.mufredatlars;
    }

    public void setMufredatlars(Set<Mufredat> mufredats) {
        if (this.mufredatlars != null) {
            this.mufredatlars.forEach(i -> i.removeBolumler(this));
        }
        if (mufredats != null) {
            mufredats.forEach(i -> i.addBolumler(this));
        }
        this.mufredatlars = mufredats;
    }

    public Bolum mufredatlars(Set<Mufredat> mufredats) {
        this.setMufredatlars(mufredats);
        return this;
    }

    public Bolum addMufredatlar(Mufredat mufredat) {
        this.mufredatlars.add(mufredat);
        mufredat.getBolumlers().add(this);
        return this;
    }

    public Bolum removeMufredatlar(Mufredat mufredat) {
        this.mufredatlars.remove(mufredat);
        mufredat.getBolumlers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bolum)) {
            return false;
        }
        return id != null && id.equals(((Bolum) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bolum{" +
            "id=" + getId() +
            ", bolumBaslik='" + getBolumBaslik() + "'" +
            ", dokuman='" + getDokuman() + "'" +
            ", puan=" + getPuan() +
            ", videoLink='" + getVideoLink() + "'" +
            ", sure='" + getSure() + "'" +
            ", sira=" + getSira() +
            "}";
    }
}
