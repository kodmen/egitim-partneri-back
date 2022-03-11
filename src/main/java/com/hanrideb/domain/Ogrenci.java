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
 * A Ogrenci.
 */
@Entity
@Table(name = "ogrenci")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ogrenci implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "level")
    private Long level;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "toplam_puan")
    private Integer toplamPuan;

    @OneToOne
    @JoinColumn(unique = true)
    private User studentUser;

    @OneToMany(mappedBy = "kayitOgrenci")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dersAnalizleris", "aitOldDers", "kayitOgrenci" }, allowSetters = true)
    private Set<Kayit> kayitlar2s = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_ogrenci__rozetler",
        joinColumns = @JoinColumn(name = "ogrenci_id"),
        inverseJoinColumns = @JoinColumn(name = "rozetler_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ogrencilers" }, allowSetters = true)
    private Set<Rozet> rozetlers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ogrenci id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLevel() {
        return this.level;
    }

    public Ogrenci level(Long level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Ogrenci aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Integer getToplamPuan() {
        return this.toplamPuan;
    }

    public Ogrenci toplamPuan(Integer toplamPuan) {
        this.setToplamPuan(toplamPuan);
        return this;
    }

    public void setToplamPuan(Integer toplamPuan) {
        this.toplamPuan = toplamPuan;
    }

    public User getStudentUser() {
        return this.studentUser;
    }

    public void setStudentUser(User user) {
        this.studentUser = user;
    }

    public Ogrenci studentUser(User user) {
        this.setStudentUser(user);
        return this;
    }

    public Set<Kayit> getKayitlar2s() {
        return this.kayitlar2s;
    }

    public void setKayitlar2s(Set<Kayit> kayits) {
        if (this.kayitlar2s != null) {
            this.kayitlar2s.forEach(i -> i.setKayitOgrenci(null));
        }
        if (kayits != null) {
            kayits.forEach(i -> i.setKayitOgrenci(this));
        }
        this.kayitlar2s = kayits;
    }

    public Ogrenci kayitlar2s(Set<Kayit> kayits) {
        this.setKayitlar2s(kayits);
        return this;
    }

    public Ogrenci addKayitlar2(Kayit kayit) {
        this.kayitlar2s.add(kayit);
        kayit.setKayitOgrenci(this);
        return this;
    }

    public Ogrenci removeKayitlar2(Kayit kayit) {
        this.kayitlar2s.remove(kayit);
        kayit.setKayitOgrenci(null);
        return this;
    }

    public Set<Rozet> getRozetlers() {
        return this.rozetlers;
    }

    public void setRozetlers(Set<Rozet> rozets) {
        this.rozetlers = rozets;
    }

    public Ogrenci rozetlers(Set<Rozet> rozets) {
        this.setRozetlers(rozets);
        return this;
    }

    public Ogrenci addRozetler(Rozet rozet) {
        this.rozetlers.add(rozet);
        rozet.getOgrencilers().add(this);
        return this;
    }

    public Ogrenci removeRozetler(Rozet rozet) {
        this.rozetlers.remove(rozet);
        rozet.getOgrencilers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ogrenci)) {
            return false;
        }
        return id != null && id.equals(((Ogrenci) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ogrenci{" +
            "id=" + getId() +
            ", level=" + getLevel() +
            ", aciklama='" + getAciklama() + "'" +
            ", toplamPuan=" + getToplamPuan() +
            "}";
    }
}
