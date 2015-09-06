package yoan.shopping.client.app.repository.mongo;

import static org.fest.assertions.api.Assertions.assertThat;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_CREATED;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_LAST_UPDATE;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_NAME;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_OWNER_ID;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_SALT;
import static yoan.shopping.client.app.repository.mongo.ClientAppMongoConverter.FIELD_SECRET;
import static yoan.shopping.infra.db.mongo.MongoDocumentConverter.FIELD_ID;

import java.util.UUID;

import org.bson.Document;
import org.junit.Test;

import yoan.shopping.client.app.ClientApp;
import yoan.shopping.infra.util.helper.DateHelper;
import yoan.shopping.test.TestHelper;

public class ClientAppMongoConverterTest {
	
	@Test
	public void fromDocument_should_return_null_with_null_document() {
		//given
		Document nullDoc = null;
		ClientAppMongoConverter testedConverter = new ClientAppMongoConverter();
		
		//when
		ClientApp result = testedConverter.fromDocument(nullDoc);
		
		//then
		assertThat(result).isNull();
	}
	
	@Test
	public void fromDocument_should_work_with_valid_doc() {
		//given
		ClientApp expectedClientApp = TestHelper.generateRandomClientApp();
		ClientAppMongoConverter testedConverter = new ClientAppMongoConverter();
		Document doc = new Document(FIELD_ID, expectedClientApp.getId())
							.append(FIELD_NAME, expectedClientApp.getName())
							.append(FIELD_OWNER_ID, expectedClientApp.getOwnerId())
							.append(FIELD_CREATED, DateHelper.toDate(expectedClientApp.getCreationDate()))
							.append(FIELD_LAST_UPDATE, DateHelper.toDate(expectedClientApp.getLastUpdate()))
							.append(FIELD_SECRET, expectedClientApp.getSecret())
							.append(FIELD_SALT, expectedClientApp.getSalt());
		
		//when
		ClientApp result = testedConverter.fromDocument(doc);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(expectedClientApp.getId());
		assertThat(result.getName()).isEqualTo(expectedClientApp.getName());
		assertThat(result.getOwnerId()).isEqualTo(expectedClientApp.getOwnerId());
		assertThat(result.getCreationDate()).isEqualTo(expectedClientApp.getCreationDate());
		assertThat(result.getLastUpdate()).isEqualTo(expectedClientApp.getLastUpdate());
		assertThat(result.getSecret()).isEqualTo(expectedClientApp.getSecret());
		assertThat(result.getSalt()).isEqualTo(expectedClientApp.getSalt());
	}
	
	@Test
	public void toDocument_should_return_empty_doc_with_null_client_app() {
		//given
		ClientApp nullClientApp = null;
		ClientAppMongoConverter testedConverter = new ClientAppMongoConverter();
		
		//when
		Document result = testedConverter.toDocument(nullClientApp);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(new Document());
	}
	
	@Test
	public void toDocument_should_work_with_valid_list() {
		//given
		ClientApp clientApp = TestHelper.generateRandomClientApp();
		ClientAppMongoConverter testedConverter = new ClientAppMongoConverter();
		
		//when
		Document result = testedConverter.toDocument(clientApp);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.get(FIELD_ID, UUID.class)).isEqualTo(clientApp.getId());
		assertThat(result.getString(FIELD_NAME)).isEqualTo(clientApp.getName());
		assertThat(result.get(FIELD_OWNER_ID, UUID.class)).isEqualTo(clientApp.getOwnerId());
		assertThat(DateHelper.toLocalDateTime(result.getDate(FIELD_CREATED))).isEqualTo(clientApp.getCreationDate());
		assertThat(DateHelper.toLocalDateTime(result.getDate(FIELD_LAST_UPDATE))).isEqualTo(clientApp.getLastUpdate());
		assertThat(result.getString(FIELD_SECRET)).isEqualTo(clientApp.getSecret());
		assertThat(result.get(FIELD_SALT)).isEqualTo(clientApp.getSalt());
	}
	
	@Test
	public void getChangeSecretUpdate_should_be_correct() {
		//given
		ClientApp clientApp = TestHelper.generateRandomClientApp();
		ClientAppMongoConverter testedConverter = new ClientAppMongoConverter();
		
		//when
		Document result = testedConverter.getChangeSecretUpdate(clientApp);
		
		//then
		assertThat(result).isNotNull();
		Document updateDoc = result.get("$set", Document.class);
		assertThat(updateDoc).isNotNull();
		assertThat(DateHelper.toLocalDateTime(updateDoc.getDate(FIELD_LAST_UPDATE))).isEqualTo(clientApp.getLastUpdate());
		assertThat(updateDoc.getString(FIELD_SECRET)).isEqualTo(clientApp.getSecret());
		assertThat(updateDoc.get(FIELD_SALT)).isEqualTo(clientApp.getSalt());
	}
}
