package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Ders.
 */
@Entity
@Table(name = "ders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "isim", length = 500)
    private String isim;

    @Column(name = "toplam_puan")
    private Integer toplamPuan;

    @Column(name = "olusturulma_tarih")
    private LocalDate olusturulmaTarih;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "aciklama")
    private String aciklama;

    @Lob
    @Column(name = "resim")
    private byte[] resim;

    @Column(name = "resim_content_type")
    private String resimContentType;

    @JsonIgnoreProperties(value = { "bolumlers", "mufredatDers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Mufredat dersMufredat;

    @JsonIgnoreProperties(value = { "yorumlar1s", "formDers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Form dersForm;

    @OneToMany(mappedBy = "aitOldDers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dersAnalizleris", "aitOldDers", "kayitOgrenci" }, allowSetters = true)
    private Set<Kayit> kayitlar1s = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ogretmenUser", "derslers" }, allowSetters = true)
    private Ogretmen dersOgretmeni;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsim() {
        return this.isim;
    }

    public Ders isim(String isim) {
        this.setIsim(isim);
        return this;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public Integer getToplamPuan() {
        return this.toplamPuan;
    }

    public Ders toplamPuan(Integer toplamPuan) {
        this.setToplamPuan(toplamPuan);
        return this;
    }

    public void setToplamPuan(Integer toplamPuan) {
        this.toplamPuan = toplamPuan;
    }

    public LocalDate getOlusturulmaTarih() {
        return this.olusturulmaTarih;
    }

    public Ders olusturulmaTarih(LocalDate olusturulmaTarih) {
        this.setOlusturulmaTarih(olusturulmaTarih);
        return this;
    }

    public void setOlusturulmaTarih(LocalDate olusturulmaTarih) {
        this.olusturulmaTarih = olusturulmaTarih;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Ders aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public byte[] getResim() {
        return this.resim;
    }

    public Ders resim(byte[] resim) {
        this.setResim(resim);
        return this;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }

    public String getResimContentType() {
        return this.resimContentType;
    }

    public Ders resimContentType(String resimContentType) {
        this.resimContentType = resimContentType;
        return this;
    }

    public void setResimContentType(String resimContentType) {
        this.resimContentType = resimContentType;
    }

    public Mufredat getDersMufredat() {
        return this.dersMufredat;
    }

    public void setDersMufredat(Mufredat mufredat) {
        this.dersMufredat = mufredat;
    }

    public Ders dersMufredat(Mufredat mufredat) {
        this.setDersMufredat(mufredat);
        return this;
    }

    public Form getDersForm() {
        return this.dersForm;
    }

    public void setDersForm(Form form) {
        this.dersForm = form;
    }

    public Ders dersForm(Form form) {
        this.setDersForm(form);
        return this;
    }

    public Set<Kayit> getKayitlar1s() {
        return this.kayitlar1s;
    }

    public void setKayitlar1s(Set<Kayit> kayits) {
        if (this.kayitlar1s != null) {
            this.kayitlar1s.forEach(i -> i.setAitOldDers(null));
        }
        if (kayits != null) {
            kayits.forEach(i -> i.setAitOldDers(this));
        }
        this.kayitlar1s = kayits;
    }

    public Ders kayitlar1s(Set<Kayit> kayits) {
        this.setKayitlar1s(kayits);
        return this;
    }

    public Ders addKayitlar1(Kayit kayit) {
        this.kayitlar1s.add(kayit);
        kayit.setAitOldDers(this);
        return this;
    }

    public Ders removeKayitlar1(Kayit kayit) {
        this.kayitlar1s.remove(kayit);
        kayit.setAitOldDers(null);
        return this;
    }

    public Ogretmen getDersOgretmeni() {
        return this.dersOgretmeni;
    }

    public void setDersOgretmeni(Ogretmen ogretmen) {
        this.dersOgretmeni = ogretmen;
    }

    public Ders dersOgretmeni(Ogretmen ogretmen) {
        this.setDersOgretmeni(ogretmen);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ders)) {
            return false;
        }
        return id != null && id.equals(((Ders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ders{" +
            "id=" + getId() +
            ", isim='" + getIsim() + "'" +
            ", toplamPuan=" + getToplamPuan() +
            ", olusturulmaTarih='" + getOlusturulmaTarih() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", resim='" + getResim() + "'" +
            ", resimContentType='" + getResimContentType() + "'" +
            "}";
    }
}
