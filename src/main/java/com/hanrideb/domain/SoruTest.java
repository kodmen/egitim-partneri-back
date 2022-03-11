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
 * A SoruTest.
 */
@Entity
@Table(name = "soru_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoruTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 500)
    @Column(name = "tes_baslik", length = 500)
    private String tesBaslik;

    @Size(max = 500)
    @Column(name = "test_pdf", length = 500)
    private String testPdf;

    @Lob
    @Column(name = "test_foto")
    private byte[] testFoto;

    @Column(name = "test_foto_content_type")
    private String testFotoContentType;

    @ManyToMany
    @JoinTable(
        name = "rel_soru_test__sorular",
        joinColumns = @JoinColumn(name = "soru_test_id"),
        inverseJoinColumns = @JoinColumn(name = "sorular_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kazanimlars", "aitOldTestlers" }, allowSetters = true)
    private Set<Soru> sorulars = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "analizBolums", "testlers", "mufredatlars" }, allowSetters = true)
    private Bolum testBolum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SoruTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTesBaslik() {
        return this.tesBaslik;
    }

    public SoruTest tesBaslik(String tesBaslik) {
        this.setTesBaslik(tesBaslik);
        return this;
    }

    public void setTesBaslik(String tesBaslik) {
        this.tesBaslik = tesBaslik;
    }

    public String getTestPdf() {
        return this.testPdf;
    }

    public SoruTest testPdf(String testPdf) {
        this.setTestPdf(testPdf);
        return this;
    }

    public void setTestPdf(String testPdf) {
        this.testPdf = testPdf;
    }

    public byte[] getTestFoto() {
        return this.testFoto;
    }

    public SoruTest testFoto(byte[] testFoto) {
        this.setTestFoto(testFoto);
        return this;
    }

    public void setTestFoto(byte[] testFoto) {
        this.testFoto = testFoto;
    }

    public String getTestFotoContentType() {
        return this.testFotoContentType;
    }

    public SoruTest testFotoContentType(String testFotoContentType) {
        this.testFotoContentType = testFotoContentType;
        return this;
    }

    public void setTestFotoContentType(String testFotoContentType) {
        this.testFotoContentType = testFotoContentType;
    }

    public Set<Soru> getSorulars() {
        return this.sorulars;
    }

    public void setSorulars(Set<Soru> sorus) {
        this.sorulars = sorus;
    }

    public SoruTest sorulars(Set<Soru> sorus) {
        this.setSorulars(sorus);
        return this;
    }

    public SoruTest addSorular(Soru soru) {
        this.sorulars.add(soru);
        soru.getAitOldTestlers().add(this);
        return this;
    }

    public SoruTest removeSorular(Soru soru) {
        this.sorulars.remove(soru);
        soru.getAitOldTestlers().remove(this);
        return this;
    }

    public Bolum getTestBolum() {
        return this.testBolum;
    }

    public void setTestBolum(Bolum bolum) {
        this.testBolum = bolum;
    }

    public SoruTest testBolum(Bolum bolum) {
        this.setTestBolum(bolum);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoruTest)) {
            return false;
        }
        return id != null && id.equals(((SoruTest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoruTest{" +
            "id=" + getId() +
            ", tesBaslik='" + getTesBaslik() + "'" +
            ", testPdf='" + getTestPdf() + "'" +
            ", testFoto='" + getTestFoto() + "'" +
            ", testFotoContentType='" + getTestFotoContentType() + "'" +
            "}";
    }
}
