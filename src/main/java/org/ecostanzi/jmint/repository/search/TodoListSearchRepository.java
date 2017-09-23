package org.ecostanzi.jmint.repository.search;

import org.ecostanzi.jmint.domain.TodoList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TodoList entity.
 */
public interface TodoListSearchRepository extends ElasticsearchRepository<TodoList, Long> {
}
