package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestAnaliz.
 */
@Entity
@Table(name = "test_analiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestAnaliz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "dogru")
    private Integer dogru;

    @Column(name = "yanlis")
    private Integer yanlis;

    @Column(name = "bos")
    private Integer bos;

    @Column(name = "net")
    private Float net;

    @Column(name = "tamamlandi")
    private Boolean tamamlandi;

    @Column(name = "test_id")
    private Long testId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "aitOldBolum", "kayitlar3s" }, allowSetters = true)
    private DersAnaliz dersAnaliz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestAnaliz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDogru() {
        return this.dogru;
    }

    public TestAnaliz dogru(Integer dogru) {
        this.setDogru(dogru);
        return this;
    }

    public void setDogru(Integer dogru) {
        this.dogru = dogru;
    }

    public Integer getYanlis() {
        return this.yanlis;
    }

    public TestAnaliz yanlis(Integer yanlis) {
        this.setYanlis(yanlis);
        return this;
    }

    public void setYanlis(Integer yanlis) {
        this.yanlis = yanlis;
    }

    public Integer getBos() {
        return this.bos;
    }

    public TestAnaliz bos(Integer bos) {
        this.setBos(bos);
        return this;
    }

    public void setBos(Integer bos) {
        this.bos = bos;
    }

    public Float getNet() {
        return this.net;
    }

    public TestAnaliz net(Float net) {
        this.setNet(net);
        return this;
    }

    public void setNet(Float net) {
        this.net = net;
    }

    public Boolean getTamamlandi() {
        return this.tamamlandi;
    }

    public TestAnaliz tamamlandi(Boolean tamamlandi) {
        this.setTamamlandi(tamamlandi);
        return this;
    }

    public void setTamamlandi(Boolean tamamlandi) {
        this.tamamlandi = tamamlandi;
    }

    public Long getTestId() {
        return this.testId;
    }

    public TestAnaliz testId(Long testId) {
        this.setTestId(testId);
        return this;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public DersAnaliz getDersAnaliz() {
        return this.dersAnaliz;
    }

    public void setDersAnaliz(DersAnaliz dersAnaliz) {
        this.dersAnaliz = dersAnaliz;
    }

    public TestAnaliz dersAnaliz(DersAnaliz dersAnaliz) {
        this.setDersAnaliz(dersAnaliz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestAnaliz)) {
            return false;
        }
        return id != null && id.equals(((TestAnaliz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestAnaliz{" +
            "id=" + getId() +
            ", dogru=" + getDogru() +
            ", yanlis=" + getYanlis() +
            ", bos=" + getBos() +
            ", net=" + getNet() +
            ", tamamlandi='" + getTamamlandi() + "'" +
            ", testId=" + getTestId() +
            "}";
    }
}
