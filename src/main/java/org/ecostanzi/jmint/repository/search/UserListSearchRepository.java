package org.ecostanzi.jmint.repository.search;

import org.ecostanzi.jmint.domain.UserList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserList entity.
 */
public interface UserListSearchRepository extends ElasticsearchRepository<UserList, Long> {
}
