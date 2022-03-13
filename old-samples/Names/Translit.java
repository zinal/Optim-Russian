
import java.util.HashMap;

public class Translit {

private static final HashMap<Character,String> HM = new HashMap<Character,String>();

static {
  HM.put('à', "a");  HM.put('À', "A");
  HM.put('á', "b");  HM.put('Á', "B");
  HM.put('â', "v");  HM.put('Â', "V");
  HM.put('ã', "g");  HM.put('Ã', "G");
  HM.put('ä', "d");  HM.put('Ä', "D");
  HM.put('å', "e");  HM.put('Å', "E");
  HM.put('¸', "iyo");  HM.put('¨', "Iyo");
  HM.put('æ', "zh");  HM.put('Æ', "Zh");
  HM.put('ç', "z");  HM.put('Ç', "Z");
  HM.put('è', "i");  HM.put('È', "I");
  HM.put('é', "y");  HM.put('É', "Y");
  HM.put('ê', "k");  HM.put('Ê', "K");
  HM.put('ë', "l");  HM.put('Ë', "L");
  HM.put('ì', "m");  HM.put('Ì', "M");
  HM.put('í', "n");  HM.put('Í', "N");
  HM.put('î', "o");  HM.put('Î', "O");
  HM.put('ï', "p");  HM.put('Ï', "P");
  HM.put('ð', "r");  HM.put('Ð', "R");
  HM.put('ñ', "s");  HM.put('Ñ', "S");
  HM.put('ò', "t");  HM.put('Ò', "T");
  HM.put('ó', "u");  HM.put('Ó', "U");
  HM.put('ô', "f");  HM.put('Ô', "F");
  HM.put('õ', "h");  HM.put('Õ', "h");
  HM.put('ö', "ts");  HM.put('Ö', "Ts");
  HM.put('÷', "ch");  HM.put('×', "Ch");
  HM.put('ø', "sh");  HM.put('Ø', "Sh");
  HM.put('ù', "stch");  HM.put('Ù', "Stch");
  HM.put('ú', "'");  HM.put('Ú', "'");
  HM.put('û', "yi");  HM.put('Û', "Yi");
  HM.put('ü', "'");  HM.put('Ü', "'");
  HM.put('ý', "e");  HM.put('Ý', "E");
  HM.put('þ', "ju");  HM.put('Þ', "Ju");
  HM.put('ÿ', "ja");  HM.put('ß', "Ja");
}

public static String map(String arg) {
  if (arg==null) return null;
  final StringBuilder sb = new StringBuilder();
  for (char c : arg.toCharArray()) {
    String v = HM.get(c);
    if (v==null) 
	sb.append(c);
    else
	sb.append(v);
  }
  return sb.toString();
}

}
