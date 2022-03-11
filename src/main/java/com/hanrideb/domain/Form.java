package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Form.
 */
@Entity
@Table(name = "form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "baslik")
    private String baslik;

    @OneToMany(mappedBy = "formYorum")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userYorum", "formYorum" }, allowSetters = true)
    private Set<Yorum> yorumlar1s = new HashSet<>();

    @JsonIgnoreProperties(value = { "dersMufredat", "dersForm", "kayitlar1s", "dersOgretmeni" }, allowSetters = true)
    @OneToOne(mappedBy = "dersForm")
    private Ders formDers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Form id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return this.baslik;
    }

    public Form baslik(String baslik) {
        this.setBaslik(baslik);
        return this;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public Set<Yorum> getYorumlar1s() {
        return this.yorumlar1s;
    }

    public void setYorumlar1s(Set<Yorum> yorums) {
        if (this.yorumlar1s != null) {
            this.yorumlar1s.forEach(i -> i.setFormYorum(null));
        }
        if (yorums != null) {
            yorums.forEach(i -> i.setFormYorum(this));
        }
        this.yorumlar1s = yorums;
    }

    public Form yorumlar1s(Set<Yorum> yorums) {
        this.setYorumlar1s(yorums);
        return this;
    }

    public Form addYorumlar1(Yorum yorum) {
        this.yorumlar1s.add(yorum);
        yorum.setFormYorum(this);
        return this;
    }

    public Form removeYorumlar1(Yorum yorum) {
        this.yorumlar1s.remove(yorum);
        yorum.setFormYorum(null);
        return this;
    }

    public Ders getFormDers() {
        return this.formDers;
    }

    public void setFormDers(Ders ders) {
        if (this.formDers != null) {
            this.formDers.setDersForm(null);
        }
        if (ders != null) {
            ders.setDersForm(this);
        }
        this.formDers = ders;
    }

    public Form formDers(Ders ders) {
        this.setFormDers(ders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Form)) {
            return false;
        }
        return id != null && id.equals(((Form) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Form{" +
            "id=" + getId() +
            ", baslik='" + getBaslik() + "'" +
            "}";
    }
}
