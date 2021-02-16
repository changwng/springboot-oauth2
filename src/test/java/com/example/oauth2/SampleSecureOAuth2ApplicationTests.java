package com.example.oauth2;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests confirming behavior of minimally-configured Authorization Server
 *
 * @author Josh Cummings
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
public class SampleSecureOAuth2ApplicationTests {
/**
 * curl -X POST http://localhost:8080/oauth/token -H "Authorization: Basic Y2xpZW50OnBhc3N3b3Jk" 
 *  -H "Content-Type: application/x-www-form-urlencoded" -d "username=user&password=pass&grant_type=password&scope=read_profile"
{Authorization=[Basic Y2xpZW50OnBhc3M=]}
curl -X POST \
'http://localhost:8080/oauth/check_token' \
-d 'token=a1849c64-2ca6-4d32-8f15-6cf01029dfb6'

 */
	private static final String CLIENT_ID = "client";

	//private static final String CLIENT_SECRET = "Y2xpZW50OnBhc3N3b3Jk";
	private static final String CLIENT_SECRET = "pass";

	private static final RequestPostProcessor CLIENT_CREDENTIALS = httpBasic(CLIENT_ID, CLIENT_SECRET);
	private static final String bearerTok ="Y2xpZW50OnBhc3N3b3Jk";
	@Autowired
	MockMvc mvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test 	public void tokenWhenUsingClientCredentialsThenIsValid() throws Exception {
		MvcResult result = this.mvc
				.perform(post("/oauth/token").with(bearerToken(bearerTok)).param("username", "user")
						.param("password", "pass")
						.param("grant_type", "password")
						// 4. client_credentials
						.param("scope","read_profile"))
				.andExpect(status().isOk()).andReturn();

		String accessToken = extract(result, "access_token");
		System.out.println("accessToken:"+accessToken);
		//403 Forbidden on
		/* @Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
		{
		oauthServer.checkTokenAccess("isAuthenticated()");    
		}
*/
		result = this.mvc.perform(post("/oauth/check_token").with(bearerToken(bearerTok)).param("token", accessToken))
				.andReturn();

		System.out.println("result:"+result);
		assertThat(Boolean.valueOf(extract(result, "active"))).isTrue();
	}

	private String extract(MvcResult result, String property) throws Exception {
		return this.objectMapper.readValue(result.getResponse().getContentAsString(), Map.class).get(property)
				.toString();
	}

	private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {

		private String token;

		public BearerTokenRequestPostProcessor(String token) {
			this.token = token;
		}

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
		//	request.addHeader("Authorization", "Bearer " + this.token);
		 	request.addHeader("Authorization", "Basic " + this.token);
			// "Content-Type: application/x-www-form-urlencoded" 
			request.addHeader("Content-Type", "application/x-www-form-urlencoded"); 
			return request;
		}

	}

	private static BearerTokenRequestPostProcessor bearerToken(String token) {
		return new BearerTokenRequestPostProcessor(token);
	}
}
