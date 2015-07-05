/**
 * 
 */
package yoan.shopping.user.repository.mongo;

import static org.fest.assertions.api.Assertions.assertThat;
import static yoan.shopping.user.repository.mongo.UserMongoConverter.FIELD_EMAIL;
import static yoan.shopping.user.repository.mongo.UserMongoConverter.FIELD_ID;
import static yoan.shopping.user.repository.mongo.UserMongoConverter.FIELD_NAME;

import java.util.UUID;

import org.bson.Document;
import org.junit.Test;

import yoan.shopping.test.TestHelper;
import yoan.shopping.user.User;

/**
 * 
 * @author yoan
 */
public class UserMongoConverterTest {
	
	@Test
	public void fromDocument_should_return_null_with_null_document() {
		//given
		Document nullDoc = null;
		UserMongoConverter testedConverter = new UserMongoConverter();
		
		//when
		User result = testedConverter.fromDocument(nullDoc);
		
		//then
		assertThat(result).isNull();
	}
	
	@Test
	public void fromDocument_should_work_with_valid_doc() {
		//given
		UUID expectId = UUID.randomUUID();
		String expectedName = "name";
		String expectedMail = "mail";
		Document doc = new Document(FIELD_ID, expectId.toString())
							.append(FIELD_NAME, expectedName)
							.append(FIELD_EMAIL, expectedMail);
		UserMongoConverter testedConverter = new UserMongoConverter();
		
		//when
		User result = testedConverter.fromDocument(doc);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(expectId);
		assertThat(result.getName()).isEqualTo(expectedName);
		assertThat(result.getEmail()).isEqualTo(expectedMail);
	}
	
	@Test
	public void toDocument_should_return_empty_doc_with_null_user() {
		//given
		User nulluser = null;
		UserMongoConverter testedConverter = new UserMongoConverter();
		
		//when
		Document result = testedConverter.toDocument(nulluser);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(new Document());
	}
	
	@Test
	public void toDocument_should_work_with_valid_user() {
		//given
		User user = TestHelper.generateRandomUser();
		UserMongoConverter testedConverter = new UserMongoConverter();
		
		//when
		Document result = testedConverter.toDocument(user);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.get(FIELD_ID)).isEqualTo(user.getId().toString());
		assertThat(result.getString(FIELD_NAME)).isEqualTo(user.getName());
		assertThat(result.getString(FIELD_EMAIL)).isEqualTo(user.getEmail());
	}
}