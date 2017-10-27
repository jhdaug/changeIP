<?php if(!defined('fb_tools')) die("Preset-File for fb_tools");

 $cfg['preset'] = array(
  'fbstd' =>    array('host' => '192.168.178.1'), # Standart-IP
  'fbata' =>    array('host' => '192.168.188.1'), # Standart-IP (ATA-Modus)
  'notfall' =>  array('host' => '169.254.1.1'),   # ZeroConf-IP (Ohne DHCP)
  'beispiel' => array('host' => 'fritz.box', 'pass' => 'geheim'), # Klassisch nur Kennwort
#  'maxbox' =>   array('host' => 'fritz.nas', 'user' => 'max', 'pass' => str_rot13('xraajbeg')), # Benutzername und Kennwort (Pseudo-Schutz)
  '7456' => unserialize(gzinflate(base64_decode('S7QysqoutjKxUsrILy5Rsi62MjQDsXNT9XJT89JTc7JT9VJSQeJAJQWJxcUgpqWVUnBqcXFmfp6ni5J1LQA'))),
 );

# $cfg['tout'] = 0; # Timeout Deaktivieren

?>
