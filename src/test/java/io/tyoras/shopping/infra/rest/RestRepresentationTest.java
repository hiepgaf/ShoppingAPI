package io.tyoras.shopping.infra.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import io.tyoras.shopping.infra.rest.Link;
import io.tyoras.shopping.infra.rest.RestRepresentation;

public class RestRepresentationTest {
	
	@Test(expected = NullPointerException.class)
	public void restRepresentation_should_fail_fast_if_null_links() {
		//given
		List<Link> nullLinks = null;
		
		//when
		try {
			new RestRepresentation(nullLinks);
		} catch(NullPointerException npe) {
		//then
			assertThat(npe.getMessage()).isEqualTo("A rest representation should always have navigation links");
			throw npe;
		}
	}
	
	@Test
	public void restRepresentation_should_work_with_empty_links() {
		//given
		List<Link> emptyLinks = Lists.newArrayList();
		
		//when
		RestRepresentation result = new RestRepresentation(emptyLinks);
		
		//then
		assertThat(result).isNotNull();
	}
}
