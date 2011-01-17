package org.springframework.social.connect.jdbc;


public class JdbcServiceProviderFactoryTest {

	// private EmbeddedDatabase db;
	//
	// private JdbcTemplate jdbcTemplate;
	//
	// private ServiceProviderFactory providerFactory;
	//
	// @Before
	// public void setup() {
	// db = new
	// SpringSocialTestDatabaseBuilder().connectedAccount().testData(getClass()).getDatabase();
	// jdbcTemplate = new JdbcTemplate(db);
	// StringEncryptor encryptor = new SearchableStringEncryptor("secret",
	// "5b8bd7612cdab5ed");
	// providerFactory = new JdbcServiceProviderFactory(jdbcTemplate,
	// encryptor);
	// }
	//
	// @After
	// public void destroy() {
	// if (db != null) {
	// db.shutdown();
	// }
	// }
	//
	// @Test
	// public void getAccountProvider() {
	// ServiceProvider<TwitterOperations> twitterProvider =
	// providerFactory.getServiceProvider("twitter",
	// TwitterOperations.class);
	// assertEquals("twitter", twitterProvider.getName());
	// assertEquals("Twitter", twitterProvider.getDisplayName());
	// assertEquals("123456789", twitterProvider.getApiKey());
	// assertEquals("http://www.twitter.com/authorize?oauth_token=123456789",
	// twitterProvider.buildAuthorizeUrl(Collections.singletonMap("requestToken",
	// "123456789")));
	//
	// ServiceProvider<FacebookOperations> facebookProvider =
	// providerFactory.getServiceProvider("facebook",
	// FacebookOperations.class);
	// assertEquals("facebook", facebookProvider.getName());
	// assertEquals("Facebook", facebookProvider.getDisplayName());
	// assertEquals("345678901", facebookProvider.getApiKey());
	// }
	//
	// @Test
	// public void getAccountProviderByName() {
	// ServiceProvider<TwitterOperations> twitterProvider =
	// providerFactory.getServiceProvider("twitter",
	// TwitterOperations.class);
	// assertEquals("twitter", twitterProvider.getName());
	// assertEquals("Twitter", twitterProvider.getDisplayName());
	// assertEquals("123456789", twitterProvider.getApiKey());
	// assertEquals("http://www.twitter.com/authorize?oauth_token=123456789",
	// twitterProvider.buildAuthorizeUrl(Collections.singletonMap("requestToken",
	// "123456789")));
	//
	// ServiceProvider<FacebookOperations> facebookProvider =
	// providerFactory.getServiceProvider("facebook",
	// FacebookOperations.class);
	// assertEquals("facebook", facebookProvider.getName());
	// assertEquals("Facebook", facebookProvider.getDisplayName());
	// assertEquals("345678901", facebookProvider.getApiKey());
	// }
}
