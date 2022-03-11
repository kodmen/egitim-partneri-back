package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DersAnaliz.
 */
@Entity
@Table(name = "ders_analiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DersAnaliz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "toplam_yanlis")
    private Integer toplamYanlis;

    @Column(name = "toplam_dogru")
    private Integer toplamDogru;

    @Column(name = "cozulen_soru")
    private Integer cozulenSoru;

    @Column(name = "tamamlandi")
    private Boolean tamamlandi;

    @ManyToOne
    @JsonIgnoreProperties(value = { "analizBolums", "testlers", "mufredatlars" }, allowSetters = true)
    private Bolum aitOldBolum;

    @ManyToMany(mappedBy = "dersAnalizleris")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dersAnalizleris", "aitOldDers", "kayitOgrenci" }, allowSetters = true)
    private Set<Kayit> kayitlar3s = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DersAnaliz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getToplamYanlis() {
        return this.toplamYanlis;
    }

    public DersAnaliz toplamYanlis(Integer toplamYanlis) {
        this.setToplamYanlis(toplamYanlis);
        return this;
    }

    public void setToplamYanlis(Integer toplamYanlis) {
        this.toplamYanlis = toplamYanlis;
    }

    public Integer getToplamDogru() {
        return this.toplamDogru;
    }

    public DersAnaliz toplamDogru(Integer toplamDogru) {
        this.setToplamDogru(toplamDogru);
        return this;
    }

    public void setToplamDogru(Integer toplamDogru) {
        this.toplamDogru = toplamDogru;
    }

    public Integer getCozulenSoru() {
        return this.cozulenSoru;
    }

    public DersAnaliz cozulenSoru(Integer cozulenSoru) {
        this.setCozulenSoru(cozulenSoru);
        return this;
    }

    public void setCozulenSoru(Integer cozulenSoru) {
        this.cozulenSoru = cozulenSoru;
    }

    public Boolean getTamamlandi() {
        return this.tamamlandi;
    }

    public DersAnaliz tamamlandi(Boolean tamamlandi) {
        this.setTamamlandi(tamamlandi);
        return this;
    }

    public void setTamamlandi(Boolean tamamlandi) {
        this.tamamlandi = tamamlandi;
    }

    public Bolum getAitOldBolum() {
        return this.aitOldBolum;
    }

    public void setAitOldBolum(Bolum bolum) {
        this.aitOldBolum = bolum;
    }

    public DersAnaliz aitOldBolum(Bolum bolum) {
        this.setAitOldBolum(bolum);
        return this;
    }

    public Set<Kayit> getKayitlar3s() {
        return this.kayitlar3s;
    }

    public void setKayitlar3s(Set<Kayit> kayits) {
        if (this.kayitlar3s != null) {
            this.kayitlar3s.forEach(i -> i.removeDersAnalizleri(this));
        }
        if (kayits != null) {
            kayits.forEach(i -> i.addDersAnalizleri(this));
        }
        this.kayitlar3s = kayits;
    }

    public DersAnaliz kayitlar3s(Set<Kayit> kayits) {
        this.setKayitlar3s(kayits);
        return this;
    }

    public DersAnaliz addKayitlar3(Kayit kayit) {
        this.kayitlar3s.add(kayit);
        kayit.getDersAnalizleris().add(this);
        return this;
    }

    public DersAnaliz removeKayitlar3(Kayit kayit) {
        this.kayitlar3s.remove(kayit);
        kayit.getDersAnalizleris().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DersAnaliz)) {
            return false;
        }
        return id != null && id.equals(((DersAnaliz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DersAnaliz{" +
            "id=" + getId() +
            ", toplamYanlis=" + getToplamYanlis() +
            ", toplamDogru=" + getToplamDogru() +
            ", cozulenSoru=" + getCozulenSoru() +
            ", tamamlandi='" + getTamamlandi() + "'" +
            "}";
    }
}
