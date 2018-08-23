package com.opt.lbch.web.rest;

import com.opt.lbch.LbchApp;

import com.opt.lbch.domain.Annonce;
import com.opt.lbch.repository.AnnonceRepository;
import com.opt.lbch.repository.search.AnnonceSearchRepository;
import com.opt.lbch.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.opt.lbch.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnnonceResource REST controller.
 *
 * @see AnnonceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LbchApp.class)
public class AnnonceResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PRIX = 1L;
    private static final Long UPDATED_PRIX = 2L;

    private static final Long DEFAULT_CATEGORIE = 1L;
    private static final Long UPDATED_CATEGORIE = 2L;

    private static final String DEFAULT_PROPRIETAIRE = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETAIRE = "BBBBBBBBBB";

    @Autowired
    private AnnonceRepository annonceRepository;


    /**
     * This repository is mocked in the com.opt.lbch.repository.search test package.
     *
     * @see com.opt.lbch.repository.search.AnnonceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnnonceSearchRepository mockAnnonceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnnonceMockMvc;

    private Annonce annonce;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnonceResource annonceResource = new AnnonceResource(annonceRepository, mockAnnonceSearchRepository);
        this.restAnnonceMockMvc = MockMvcBuilders.standaloneSetup(annonceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createEntity(EntityManager em) {
        Annonce annonce = new Annonce()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .prix(DEFAULT_PRIX)
            .categorie(DEFAULT_CATEGORIE)
            .proprietaire(DEFAULT_PROPRIETAIRE);
        return annonce;
    }

    @Before
    public void initTest() {
        annonce = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnonce() throws Exception {
        int databaseSizeBeforeCreate = annonceRepository.findAll().size();

        // Create the Annonce
        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isCreated());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate + 1);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnonce.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testAnnonce.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testAnnonce.getProprietaire()).isEqualTo(DEFAULT_PROPRIETAIRE);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).save(testAnnonce);
    }

    @Test
    @Transactional
    public void createAnnonceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annonceRepository.findAll().size();

        // Create the Annonce with an existing ID
        annonce.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(0)).save(annonce);
    }

    @Test
    @Transactional
    public void getAllAnnonces() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get all the annonceList
        restAnnonceMockMvc.perform(get("/api/annonces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.intValue())))
            .andExpect(jsonPath("$.[*].proprietaire").value(hasItem(DEFAULT_PROPRIETAIRE.toString())));
    }
    

    @Test
    @Transactional
    public void getAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get the annonce
        restAnnonceMockMvc.perform(get("/api/annonces/{id}", annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(annonce.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.intValue()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.intValue()))
            .andExpect(jsonPath("$.proprietaire").value(DEFAULT_PROPRIETAIRE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAnnonce() throws Exception {
        // Get the annonce
        restAnnonceMockMvc.perform(get("/api/annonces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Update the annonce
        Annonce updatedAnnonce = annonceRepository.findById(annonce.getId()).get();
        // Disconnect from session so that the updates on updatedAnnonce are not directly saved in db
        em.detach(updatedAnnonce);
        updatedAnnonce
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .prix(UPDATED_PRIX)
            .categorie(UPDATED_CATEGORIE)
            .proprietaire(UPDATED_PROPRIETAIRE);

        restAnnonceMockMvc.perform(put("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnnonce)))
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAnnonce.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnonce.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testAnnonce.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testAnnonce.getProprietaire()).isEqualTo(UPDATED_PROPRIETAIRE);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).save(testAnnonce);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Create the Annonce

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restAnnonceMockMvc.perform(put("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonce)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(0)).save(annonce);
    }

    @Test
    @Transactional
    public void deleteAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeDelete = annonceRepository.findAll().size();

        // Get the annonce
        restAnnonceMockMvc.perform(delete("/api/annonces/{id}", annonce.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).deleteById(annonce.getId());
    }

    @Test
    @Transactional
    public void searchAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);
        when(mockAnnonceSearchRepository.search(queryStringQuery("id:" + annonce.getId())))
            .thenReturn(Collections.singletonList(annonce));
        // Search the annonce
        restAnnonceMockMvc.perform(get("/api/_search/annonces?query=id:" + annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.intValue())))
            .andExpect(jsonPath("$.[*].proprietaire").value(hasItem(DEFAULT_PROPRIETAIRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annonce.class);
        Annonce annonce1 = new Annonce();
        annonce1.setId(1L);
        Annonce annonce2 = new Annonce();
        annonce2.setId(annonce1.getId());
        assertThat(annonce1).isEqualTo(annonce2);
        annonce2.setId(2L);
        assertThat(annonce1).isNotEqualTo(annonce2);
        annonce1.setId(null);
        assertThat(annonce1).isNotEqualTo(annonce2);
    }
}
