/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorytrackingsystem;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
/**
 *
 * @author cbastian
 */
public class EncryptUtils{
  public static final String DEFAULT_ENCODING="UTF-8";
  static BASE64Encoder enc=new BASE64Encoder();
  static BASE64Decoder dec=new BASE64Decoder();

  public static String base64encode(String text){
    try{
      String rez = enc.encode( text.getBytes( DEFAULT_ENCODING ) );
      return rez;
    }catch(Exception e){
      return null;
    }
  }//base64encode

  public static String base64decode(String text){
    try{
      return new String(dec.decodeBuffer( text ),DEFAULT_ENCODING);
    }catch(Exception e){
      return null;
    }
  }//base64decode

  public static void main(String[] args){
    String userName="inventoryUser";
    String password="R=2eth*wahuGAQa5Az&vu7r4Ch=7a5Av";
    System.out.println(userName+"\r\n"+password);
    String encodedUN=base64encode(userName);
    String encodedPW=base64encode(password);

    System.out.println(" is encoded to: "+encodedUN+"\r\n"+encodedPW);
    String decodedUN=base64decode(encodedUN);
    String decodedPW=base64decode(encodedPW);
    System.out.println(" is decoded to: "+decodedUN+"\r\n"+decodedPW);
  }
}