package de.daug.changeIP;

import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.daug.changeIP.Config;
/**
 * 
 * @author Joern
 *
 *         Dieses Programm holt sich mithilfe der fb_tools (fritz box tools,
 *         php-Skript) die IP der angegebenen Router. Danach baut es eine
 *         Verbindung zum Tagmanger auf (TM.java) und schreibt in die
 *         angegebenen Variablen die aktuellen IPs.
 * 
 *         Wichtig ist die .p12 Datei im Ordner resources. Sie enthält das
 *         Passwort für den freigeschalteten Nutzer
 *         xxx@xxx.gserviceaccount.com.
 * 
 *         Die API des Tagmanagers muss aktiviert sein:
 *         https://console.developers.google.com/apis/dashboard?project=iptotag&authuser=0
 *         mit xxx@xxx.de erstellt.
 */
public class App {

	// lokale IPs
	private String router_1 = Config.ROUTER_1_LOCAL_IP;
	private String router_2 = Config.ROUTER_2_LOCAL_IP;

	// Variablen IDs im Tagmanager
	private static int router_1_id = Config.ROUTER_1_TAGMANGER_VARIABLE_ID;
	private static int router_2_id = Config.ROUTER_2_TAGMANGER_VARIABLE_ID;

	private String[] routers = new String[2];
	private List<String> ips = new ArrayList<String>();

	/**
	 * Konstruktor
	 */
	public App() {
		routers[0] = router_1;
		routers[1] = router_2;
		// hole oeffentliche IPs
		getIpsFromRouters();
	}

	/**
	 * sucht oeffentliche IPs der Router
	 */
	private void getIpsFromRouters() {

		// fuer jeden Router
		for (String router : routers) {

			// cmd Befehl
			String command = "cd \"" + System.getProperty("user.dir") + "\\resources\\fb_tools\" && fb_tools " + router
					+ " getip";
			
			
			// aufrufen der cmd
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
			builder.redirectErrorStream(true);
			Process p = null;
			try {
				p = builder.start();
			} catch (IOException e) {
				// System.out.println("Fehler beim Starten des
				// Processbuilder:\n" + e);
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

			// IP suchen und speichern
			String line = null;
			while (true) {
				try {
					line = r.readLine();
					// System.out.println(line);
				} catch (IOException e) {
					// System.out.println("IOException: " + e);
				}
				if (line == null) {
					break;
				}
				if (line.indexOf("IPv4") != -1) {
					// System.out.println(line);
					ips.add(line.replace("IPv4 : ", ""));
				}
			}
		}
	}

	/**
	 * liefert eine Liste mit den oeffentlichen IPs der Router
	 * @return
	 */
	public List<String> getIps() {
		return this.ips;
	}

	/**
	 * Hauptklasse
	 * @param args
	 * @throws IOException
	 * 
	 * Kommentare sind wegen dem Windows Task Scheduler ausgeblendet
	 */
	public static void main(String[] args) throws IOException {

		App app = new App();
		// System.out.println(app.getIps());
		TM tm = new TM();

		// System.out.println("Ändere IP von Router 1 zu " +
		// app.getIps().get(0));
		tm.IPchange(app.getIps().get(0), router_1_id);

		// System.out.println("Ändere IP von Router 2 zu " +
		// app.getIps().get(1));
		tm.IPchange(app.getIps().get(1), router_2_id);

		// System.out.println("Ende");
	}
}
