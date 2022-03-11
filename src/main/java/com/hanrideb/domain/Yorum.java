package com.hanrideb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Yorum.
 */
@Entity
@Table(name = "yorum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Yorum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 10000)
    @Column(name = "yazi", length = 10000)
    private String yazi;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private User userYorum;

    @ManyToOne
    @JsonIgnoreProperties(value = { "yorumlar1s", "formDers" }, allowSetters = true)
    private Form formYorum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Yorum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYazi() {
        return this.yazi;
    }

    public Yorum yazi(String yazi) {
        this.setYazi(yazi);
        return this;
    }

    public void setYazi(String yazi) {
        this.yazi = yazi;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Yorum date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUserYorum() {
        return this.userYorum;
    }

    public void setUserYorum(User user) {
        this.userYorum = user;
    }

    public Yorum userYorum(User user) {
        this.setUserYorum(user);
        return this;
    }

    public Form getFormYorum() {
        return this.formYorum;
    }

    public void setFormYorum(Form form) {
        this.formYorum = form;
    }

    public Yorum formYorum(Form form) {
        this.setFormYorum(form);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Yorum)) {
            return false;
        }
        return id != null && id.equals(((Yorum) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Yorum{" +
            "id=" + getId() +
            ", yazi='" + getYazi() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
