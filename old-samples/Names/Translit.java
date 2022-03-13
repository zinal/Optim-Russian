
import java.util.HashMap;

public class Translit {

private static final HashMap<Character,String> HM = new HashMap<Character,String>();

static {
  HM.put('�', "a");  HM.put('�', "A");
  HM.put('�', "b");  HM.put('�', "B");
  HM.put('�', "v");  HM.put('�', "V");
  HM.put('�', "g");  HM.put('�', "G");
  HM.put('�', "d");  HM.put('�', "D");
  HM.put('�', "e");  HM.put('�', "E");
  HM.put('�', "iyo");  HM.put('�', "Iyo");
  HM.put('�', "zh");  HM.put('�', "Zh");
  HM.put('�', "z");  HM.put('�', "Z");
  HM.put('�', "i");  HM.put('�', "I");
  HM.put('�', "y");  HM.put('�', "Y");
  HM.put('�', "k");  HM.put('�', "K");
  HM.put('�', "l");  HM.put('�', "L");
  HM.put('�', "m");  HM.put('�', "M");
  HM.put('�', "n");  HM.put('�', "N");
  HM.put('�', "o");  HM.put('�', "O");
  HM.put('�', "p");  HM.put('�', "P");
  HM.put('�', "r");  HM.put('�', "R");
  HM.put('�', "s");  HM.put('�', "S");
  HM.put('�', "t");  HM.put('�', "T");
  HM.put('�', "u");  HM.put('�', "U");
  HM.put('�', "f");  HM.put('�', "F");
  HM.put('�', "h");  HM.put('�', "h");
  HM.put('�', "ts");  HM.put('�', "Ts");
  HM.put('�', "ch");  HM.put('�', "Ch");
  HM.put('�', "sh");  HM.put('�', "Sh");
  HM.put('�', "stch");  HM.put('�', "Stch");
  HM.put('�', "'");  HM.put('�', "'");
  HM.put('�', "yi");  HM.put('�', "Yi");
  HM.put('�', "'");  HM.put('�', "'");
  HM.put('�', "e");  HM.put('�', "E");
  HM.put('�', "ju");  HM.put('�', "Ju");
  HM.put('�', "ja");  HM.put('�', "Ja");
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
