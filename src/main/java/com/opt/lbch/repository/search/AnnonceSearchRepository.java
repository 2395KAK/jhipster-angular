package com.opt.lbch.repository.search;

import com.opt.lbch.domain.Annonce;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Annonce entity.
 */
public interface AnnonceSearchRepository extends ElasticsearchRepository<Annonce, Long> {
}
