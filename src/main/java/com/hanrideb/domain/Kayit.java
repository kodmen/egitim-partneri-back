package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Kayit.
 */
@Entity
@Table(name = "kayit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Kayit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "puan")
    private Integer puan;

    @Column(name = "kayit_tarih")
    private LocalDate kayitTarih;

    @ManyToMany
    @JoinTable(
        name = "rel_kayit__ders_analizleri",
        joinColumns = @JoinColumn(name = "kayit_id"),
        inverseJoinColumns = @JoinColumn(name = "ders_analizleri_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aitOldBolum", "kayitlar3s" }, allowSetters = true)
    private Set<DersAnaliz> dersAnalizleris = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "dersMufredat", "dersForm", "kayitlar1s", "dersOgretmeni" }, allowSetters = true)
    private Ders aitOldDers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "studentUser", "kayitlar2s", "rozetlers" }, allowSetters = true)
    private Ogrenci kayitOgrenci;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Kayit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuan() {
        return this.puan;
    }

    public Kayit puan(Integer puan) {
        this.setPuan(puan);
        return this;
    }

    public void setPuan(Integer puan) {
        this.puan = puan;
    }

    public LocalDate getKayitTarih() {
        return this.kayitTarih;
    }

    public Kayit kayitTarih(LocalDate kayitTarih) {
        this.setKayitTarih(kayitTarih);
        return this;
    }

    public void setKayitTarih(LocalDate kayitTarih) {
        this.kayitTarih = kayitTarih;
    }

    public Set<DersAnaliz> getDersAnalizleris() {
        return this.dersAnalizleris;
    }

    public void setDersAnalizleris(Set<DersAnaliz> dersAnalizs) {
        this.dersAnalizleris = dersAnalizs;
    }

    public Kayit dersAnalizleris(Set<DersAnaliz> dersAnalizs) {
        this.setDersAnalizleris(dersAnalizs);
        return this;
    }

    public Kayit addDersAnalizleri(DersAnaliz dersAnaliz) {
        this.dersAnalizleris.add(dersAnaliz);
        dersAnaliz.getKayitlar3s().add(this);
        return this;
    }

    public Kayit removeDersAnalizleri(DersAnaliz dersAnaliz) {
        this.dersAnalizleris.remove(dersAnaliz);
        dersAnaliz.getKayitlar3s().remove(this);
        return this;
    }

    public Ders getAitOldDers() {
        return this.aitOldDers;
    }

    public void setAitOldDers(Ders ders) {
        this.aitOldDers = ders;
    }

    public Kayit aitOldDers(Ders ders) {
        this.setAitOldDers(ders);
        return this;
    }

    public Ogrenci getKayitOgrenci() {
        return this.kayitOgrenci;
    }

    public void setKayitOgrenci(Ogrenci ogrenci) {
        this.kayitOgrenci = ogrenci;
    }

    public Kayit kayitOgrenci(Ogrenci ogrenci) {
        this.setKayitOgrenci(ogrenci);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kayit)) {
            return false;
        }
        return id != null && id.equals(((Kayit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kayit{" +
            "id=" + getId() +
            ", puan=" + getPuan() +
            ", kayitTarih='" + getKayitTarih() + "'" +
            "}";
    }

    public static Kayit bosKayitUret() {
        Kayit kayit = new Kayit();
        kayit.kayitTarih(LocalDate.now());
        kayit.setPuan(0);
        kayit.setDersAnalizleris(new HashSet<DersAnaliz>());
        return kayit;
    }
}
