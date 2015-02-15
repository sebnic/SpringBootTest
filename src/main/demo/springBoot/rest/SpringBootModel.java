package demo.springBoot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringBootModel {

	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	public SpringBootModel(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}
	
}
