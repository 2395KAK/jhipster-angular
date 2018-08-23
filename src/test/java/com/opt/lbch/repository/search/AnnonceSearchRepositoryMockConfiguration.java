package com.opt.lbch.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AnnonceSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AnnonceSearchRepositoryMockConfiguration {

    @MockBean
    private AnnonceSearchRepository mockAnnonceSearchRepository;

}
