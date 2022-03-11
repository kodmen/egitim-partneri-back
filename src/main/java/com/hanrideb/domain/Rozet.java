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
 * A Rozet.
 */
@Entity
@Table(name = "rozet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rozet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "rozet_ismi", length = 500)
    private String rozetIsmi;

    @Lob
    @Column(name = "rozet_resim")
    private byte[] rozetResim;

    @Column(name = "rozet_resim_content_type")
    private String rozetResimContentType;

    @ManyToMany(mappedBy = "rozetlers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studentUser", "kayitlar2s", "rozetlers" }, allowSetters = true)
    private Set<Ogrenci> ogrencilers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rozet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRozetIsmi() {
        return this.rozetIsmi;
    }

    public Rozet rozetIsmi(String rozetIsmi) {
        this.setRozetIsmi(rozetIsmi);
        return this;
    }

    public void setRozetIsmi(String rozetIsmi) {
        this.rozetIsmi = rozetIsmi;
    }

    public byte[] getRozetResim() {
        return this.rozetResim;
    }

    public Rozet rozetResim(byte[] rozetResim) {
        this.setRozetResim(rozetResim);
        return this;
    }

    public void setRozetResim(byte[] rozetResim) {
        this.rozetResim = rozetResim;
    }

    public String getRozetResimContentType() {
        return this.rozetResimContentType;
    }

    public Rozet rozetResimContentType(String rozetResimContentType) {
        this.rozetResimContentType = rozetResimContentType;
        return this;
    }

    public void setRozetResimContentType(String rozetResimContentType) {
        this.rozetResimContentType = rozetResimContentType;
    }

    public Set<Ogrenci> getOgrencilers() {
        return this.ogrencilers;
    }

    public void setOgrencilers(Set<Ogrenci> ogrencis) {
        if (this.ogrencilers != null) {
            this.ogrencilers.forEach(i -> i.removeRozetler(this));
        }
        if (ogrencis != null) {
            ogrencis.forEach(i -> i.addRozetler(this));
        }
        this.ogrencilers = ogrencis;
    }

    public Rozet ogrencilers(Set<Ogrenci> ogrencis) {
        this.setOgrencilers(ogrencis);
        return this;
    }

    public Rozet addOgrenciler(Ogrenci ogrenci) {
        this.ogrencilers.add(ogrenci);
        ogrenci.getRozetlers().add(this);
        return this;
    }

    public Rozet removeOgrenciler(Ogrenci ogrenci) {
        this.ogrencilers.remove(ogrenci);
        ogrenci.getRozetlers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rozet)) {
            return false;
        }
        return id != null && id.equals(((Rozet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rozet{" +
            "id=" + getId() +
            ", rozetIsmi='" + getRozetIsmi() + "'" +
            ", rozetResim='" + getRozetResim() + "'" +
            ", rozetResimContentType='" + getRozetResimContentType() + "'" +
            "}";
    }
}
