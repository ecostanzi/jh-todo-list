package org.ecostanzi.jmint.repository.search;

import org.ecostanzi.jmint.domain.Todo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Todo entity.
 */
public interface TodoSearchRepository extends ElasticsearchRepository<Todo, Long> {
}
