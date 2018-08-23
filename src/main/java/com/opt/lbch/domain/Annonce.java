package com.opt.lbch.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
@Document(indexName = "annonce")
public class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "prix")
    private Long prix;

    @Column(name = "categorie")
    private Long categorie;

    @Column(name = "proprietaire")
    private String proprietaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Annonce titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public Annonce description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrix() {
        return prix;
    }

    public Annonce prix(Long prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Long prix) {
        this.prix = prix;
    }

    public Long getCategorie() {
        return categorie;
    }

    public Annonce categorie(Long categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Long categorie) {
        this.categorie = categorie;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public Annonce proprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
        return this;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Annonce annonce = (Annonce) o;
        if (annonce.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annonce.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Annonce{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", prix=" + getPrix() +
            ", categorie=" + getCategorie() +
            ", proprietaire='" + getProprietaire() + "'" +
            "}";
    }
}
