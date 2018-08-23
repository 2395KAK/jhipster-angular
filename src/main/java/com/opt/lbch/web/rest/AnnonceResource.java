package com.opt.lbch.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opt.lbch.domain.Annonce;
import com.opt.lbch.repository.AnnonceRepository;
import com.opt.lbch.repository.search.AnnonceSearchRepository;
import com.opt.lbch.web.rest.errors.BadRequestAlertException;
import com.opt.lbch.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Annonce.
 */
@RestController
@RequestMapping("/api")
public class AnnonceResource {

    private final Logger log = LoggerFactory.getLogger(AnnonceResource.class);

    private static final String ENTITY_NAME = "annonce";

    private final AnnonceRepository annonceRepository;

    private final AnnonceSearchRepository annonceSearchRepository;

    public AnnonceResource(AnnonceRepository annonceRepository, AnnonceSearchRepository annonceSearchRepository) {
        this.annonceRepository = annonceRepository;
        this.annonceSearchRepository = annonceSearchRepository;
    }

    /**
     * POST  /annonces : Create a new annonce.
     *
     * @param annonce the annonce to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annonce, or with status 400 (Bad Request) if the annonce has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/annonces")
    @Timed
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) throws URISyntaxException {
        log.debug("REST request to save Annonce : {}", annonce);
        if (annonce.getId() != null) {
            throw new BadRequestAlertException("A new annonce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annonce result = annonceRepository.save(annonce);
        annonceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/annonces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annonces : Updates an existing annonce.
     *
     * @param annonce the annonce to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annonce,
     * or with status 400 (Bad Request) if the annonce is not valid,
     * or with status 500 (Internal Server Error) if the annonce couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/annonces")
    @Timed
    public ResponseEntity<Annonce> updateAnnonce(@RequestBody Annonce annonce) throws URISyntaxException {
        log.debug("REST request to update Annonce : {}", annonce);
        if (annonce.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Annonce result = annonceRepository.save(annonce);
        annonceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, annonce.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annonces : get all the annonces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of annonces in body
     */
    @GetMapping("/annonces")
    @Timed
    public List<Annonce> getAllAnnonces() {
        log.debug("REST request to get all Annonces");
        return annonceRepository.findAll();
    }

    /**
     * GET  /annonces/:id : get the "id" annonce.
     *
     * @param id the id of the annonce to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annonce, or with status 404 (Not Found)
     */
    @GetMapping("/annonces/{id}")
    @Timed
    public ResponseEntity<Annonce> getAnnonce(@PathVariable Long id) {
        log.debug("REST request to get Annonce : {}", id);
        Optional<Annonce> annonce = annonceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(annonce);
    }

    /**
     * DELETE  /annonces/:id : delete the "id" annonce.
     *
     * @param id the id of the annonce to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/annonces/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        log.debug("REST request to delete Annonce : {}", id);

        annonceRepository.deleteById(id);
        annonceSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/annonces?query=:query : search for the annonce corresponding
     * to the query.
     *
     * @param query the query of the annonce search
     * @return the result of the search
     */
    @GetMapping("/_search/annonces")
    @Timed
    public List<Annonce> searchAnnonces(@RequestParam String query) {
        log.debug("REST request to search Annonces for query {}", query);
        return StreamSupport
            .stream(annonceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
