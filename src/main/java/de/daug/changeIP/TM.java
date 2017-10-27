package de.daug.changeIP;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.TagManagerScopes;
import com.google.api.services.tagmanager.model.Parameter;
import com.google.api.services.tagmanager.model.Variable;

import de.daug.changeIP.Config;

/**
 * Diese Klasse baut eine Verbindung zum Tag Manager auf und schreibt die
 * aktuellen IPs in die angegebenen Variablen.
 * 
 * @author Joern
 *
 */
public class TM {
	// Nutzer
	private static final String SERVICE_ACCOUNT_EMAIL = Config.SERVICE_ACCOUNT_EMAIL;

	// Passwort-Datei
	private static final String P12_AUTH_KEY_FILEPATH = System.getProperty("user.dir") + "\\resources\\"
			+ Config.P12_FILE_NAME;

	private static final String APPLICATION_NAME = "Authorization";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static NetHttpTransport httpTransport;

	private static ArrayList<String> gaGTMScopes;

	/**
	 * leerer Konstruktor
	 */
	public TM() {
	}

	/**
	 * Aendern der IP
	 * 
	 * @param ip: zu aendernde IP
	 * @param routerVariableId: Variablen ID im Tag Manager
	 */
	public void IPchange(String ip, int routerVariableId) {
		// Specify aktivieren.
		gaGTMScopes = new ArrayList<String>();
		gaGTMScopes.addAll(TagManagerScopes.all());

		// Authorization flow.
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();

			// Anmeldung
			Credential credential = authorizeServiceAccount();

			// Verbindung zum Tag Manager aufbauen
			TagManager manager = new TagManager.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();

			// Variable abfragen
			Variable v = manager.accounts().containers().workspaces().variables()
					.get("accounts/" + Config.ACCOUNTS_ID 
						 + "/containers/" + Config.CONTAINERS_ID
						 + "/workspaces/" + Config.WORKSPACES_ID 
						 + "/variables/" + routerVariableId)
					.execute();

			List<Parameter> parameter = v.getParameter();

			// Variablenwert aendern
			parameter.get(0).setValue(ip);

			// neuen Wert aktualisieren
			manager.accounts().containers().workspaces().variables()
					.update("accounts/" + Config.ACCOUNTS_ID 
							+ "/containers/" + Config.CONTAINERS_ID 
							+ "/workspaces/" + Config.WORKSPACES_ID 
							+ "/variables/" + routerVariableId, v)
					.execute();

		} catch (IOException | GeneralSecurityException ioEx) {
			// ioEx.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Google Credentials zur Anmeldung
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Credential authorizeServiceAccount() throws Exception {
		// System.out.println(TM.class.getResourceAsStream(P12_AUTH_KEY_FILEPATH).toString());

		FileInputStream is = new FileInputStream(P12_AUTH_KEY_FILEPATH);

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, Config.P12_PASSWORD.toCharArray());

		String alias = Config.P12_ALIAS;
		KeyPair kp = null;

		Key key = keystore.getKey(alias, Config.P12_PASSWORD.toCharArray());
		if (key instanceof PrivateKey) {
			// Get certificate of public key
			Certificate cert = keystore.getCertificate(alias);

			// Get public key
			PublicKey publicKey = cert.getPublicKey();

			// Return a key pair
			kp = new KeyPair(publicKey, (PrivateKey) key);
		}

		// Construct a GoogleCredential object with the service account email
		// and p12 file downloaded from the developer console.
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountPrivateKey(kp.getPrivate())
				// .setServiceAccountPrivateKeyFromP12File((new
				// File(P12_AUTH_KEY_FILEPATH)))
				.setServiceAccountScopes(gaGTMScopes).build();
		return credential;
	}
}
