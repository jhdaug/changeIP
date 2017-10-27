<?php if(!defined('fb_tools')) die("Plugin-File for fb_tools");

 $plugin = "fbtp_unprotect 0.02 (c) 20.07.2016 by Michael Engelke <http://www.mengelke.de>"; /* (charset=iso-8859-1 / tabs=8 / lines=cr+lf)

Pluginbeschreibung:
Modifiziert die Konfiguration, um die gesch�tzten Bereiche der GUI mit "no" freizuschalten

fbtp_unprotect Programmgeschichte:
V0.01 13.07.2016 (Intern)
 - Erste Version
V0.02 20.07.2016
 - Verschiedene Bugs behoben
*/
 if(ifset($cfg['help']) or $pset == $pmax) {				// Hilfe Ausgeben
  out("$plugin\n\n$self <user:pass@fritz.box:port> Plugin {$argv[$pset-1]} [yes|no]"
  .((preg_match('/[ab]/i',$cfg['help'])) ? "\n\nBeispiel:\n$self password@fritz.box plugin {$argv[$pset-1]} no\n\n" : ""));
 }
 elseif($pset < $pmax and ifset($argv[$pset],'/^(yes|no)$/')) {		// Parameter �berpr�fen
  if($sid = (ifset($cfg['bsid'])) ? $cfg['bsid'] : login()) {		// Login durchf�hren

   if($cfg['dbug']/(1<<0)%2)						// Debug-Meldung f�r cfgexport
    dbug("Hole Konfiguration");
   $data = cfgexport(false,$cfg['pass']);				// Konfig aus der Fritz!Box holen

   if($cfg['dbug']/(1<<0)%2)						// Debug-Meldung f�r preg_replace
    dbug("Patche Konfiguration");

   $datapat = preg_replace('/(((?<!lanbridges_gui)_hidden|gui_readonly) = )(yes|no)(?=;\s*$)/m','$1'.$argv[$pset++],$data);	// Patchen

   if($data != $datapat) {						// Auf �nderung pr�fen
    if($cfg['dbug']/(1<<0)%2)						// Debug-Meldung f�r cfgimport
     dbug("Sende Konfiguration");
    out((cfgimport(false,$cfg['pass'],$datapat))				// Importieren
     ? "Konfig wurde hochgeladen und wird nun bearbeitet" : errmsg(0,'cfgimport'));	// R�ckmeldung
   }
   else
    out("Keine �nderungen vorgenommen");

   if(!ifset($cfg['bsid']))						// Abmelden
    logout($sid);
  }
  else
   out(errmsg(0,'login'));						// Login fehlgeschlagen
 }
 else
  out("Parameter muss korrekt angegeben werden");

?>
