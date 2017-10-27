<?php if(!defined('fb_tools')) die("Plugin-File for fb_tools");

 $plugin = "fbtp_led 0.03 (c) 28.01.2017 by Michael Engelke <http://www.mengelke.de>"; /* (charset=iso-8859-1 / tabs=8 / lines=cr+lf)

Pluginbeschreibung:
Schaltet die LED an oder aus

fbtp_led Programmgeschichte:
V0.01 07.07.2016
 - Erste Version
V0.02 20.07.2016
 - Verschiedene Bugs behoben
V0.03 28.01.2017
 - Neue Lösung basierend auf fritzbox_led_display_on_off_lua.php von Gregor Nathanael Meyer <Gregor@der-meyer.de>
*/
 if(ifset($cfg['help'])) {						// Hilfe Ausgeben
  out("$plugin\n\n$self <user:pass@fritz.box:port> Plugin {$argv[$pset-1]} [on|off|an|aus]\n\nBeispiel:\n$self password@fritz.box plugin {$argv[$pset-1]} off\n\n");
 }

 if($sid = (ifset($cfg['bsid'])) ? $cfg['bsid'] : login()) {		// Login durchführen

  if($pset < $pmax and $val = ifset($argv[$pset],'/([ao]n)|(off|aus)/i')) {	// Parameter überprüfen
   dbug("Schalte LED a".(($val[1]) ? "n" : "us"));
   request('POST','/system/led_display.lua',"sid=$sid&apply=&led_display=".(($val[2]) ? 2 : 0));	// LED schalten: an / aus
  }

  if($var = ifset(request('GET','/query.lua',"sid=$sid&led=box:settings/led_display"),'/"led":\s*"(\d+)"/')) {	// LED Status Abfragen
   dbug("Ermittle LED-Status");
   out("LED ist a".(($var[1]) ? "us" : "n"));				// LED Status ausgeben
  }

  if(!ifset($cfg['bsid'])) {						// Abmelden
   logout($sid);
  }
 }
 else {
  out(errmsg(0,'login'));						// Login fehlgeschlagen
 }

?>
