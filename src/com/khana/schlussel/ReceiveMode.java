package com.khana.schlussel;

/**
 * <p>STANDARD:
 * <br/>Das Stiftschloss sendet die Stiftnummer asynchron immer dann, wenn ein
 * Stift eingesteckt wird, oder sendet "OUT", wenn der Stift ausgezogen wird.
 * <br/>Antwort-Beispiel Stiftnummer wenn Stift eingesteckt wird: EE000004622D309OUT
 * <br/>Antwort immer wenn Stift ausgezogen wird: OUT
 * <br/>F�r den Verbindungstest der seriellen Schnittstelle wird im Testprogramm im 
 * Standard-Mode zus�tzlich noch alle 300ms eine '0' zum Stiftschloss gesendet.
 * Das geh�rt nicht zum Stiftschloss-Protokoll, es dient nur dazu, im Testprogramm
 * einen Verbindungsunterbruch zu erkennen. Ein Verbindungsunterbruch entsteht durch das
 * Ausziehen des USB Steckers.
 * <br/>
 * <br/>
 * <p>ENQ1, ENQ9 und ENQ1_ENQ9:
 * <br/>Hier wird das Stiftschloss im "Polling Mode" synchron betrieben, d.h. nur, 
 * wenn das Testprogramm eine '1' (ENQ1) oder '9' (ENQ9) sendet, antwortet das 
 * Stiftschloss.
 * <br/>Im Testprogramm erfolgt eine ENQ1 oder ENQ9 Abfrage alle 300ms.
 * <br/>Hinweis: Wenn das zyklische Polling l�nger als 2sec ausbleibt, wechselt das Stiftschloss
 * in den Standard-Mode. Im Testprogramm wird das nicht ber�cksichtigt. 
 * Es wird davon ausgegangen, dass das Stiftschloss entweder im Standard-Mode oder
 * im Polling Mode betrieben wird.
 * <br/>
 * <br/>
 * <p>ENQ1:
 * <br/>Das Testprogramm sendet eine '1' und bekommt bei eingestecktem Stift vom
 * Stiftschloss bei eingestecktem Stift die Stiftnummer (z.B.: 020000046223D30396) 
 * oder bei fehlendem Stift immer 020000000000000300 als Antwort.
 * <br/>
 * <br/>
 * <p>ENQ9:
 * <br/>Das Testprogramm sendet eine '9' und bekommt bei eingestecktem Stift vom
 * Stiftschloss die im EPROM des Stifts enthaltene Nummer (z.B.: 020000000000010301)
 * oder bei fehlendem Stift immer 020000000000000300 als Antwort.
 * <br/>
 * <br/>
 * <p>ENQ1_ENQ9:
 * <br/>Das Testprogramm sendet abwechslungsweise ENQ1 oder ENQ9 und f�llt die
 * erhaltene Antwort in die entsprechenden Anzeigefelder.
 *
 */
public enum ReceiveMode {
	STANDARD,ENQ1,ENQ9,ENQ1_ENQ9;
}
