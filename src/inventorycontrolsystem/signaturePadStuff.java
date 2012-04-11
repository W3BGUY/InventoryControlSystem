/*
 * @author Charles "w3bguy" Bastian -> With assistance from Chris Priesz (chris_p@topazsystems.com)
 * Class for all Signature Pad functions.
 * Part of the Inventory Tracking System, created for use at Hill College.
 * Initial Creation: 2011.11.28
 * Licensed to Hill College while I am employed there.
 */

package inventorytrackingsystem;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import gnu.io.*;
import java.io.*;
import java.io.File.*;
import java.util.*;
import java.util.Date;

import com.sun.jimi.core.*;
import com.topaz.sigplus.*;

public class signaturePadStuff extends Frame implements Runnable{
  SigPlus             sigObj = null;
  static final int    numImages = 3;
  Image[]             rawImages;
  static final String fileBase = ".\\src\\images\\";
  int                 AppScreen;
  Thread              eventThread;
  int                 currentImage;	
  int                 page;
  String              signature;
  String              newFileName;
  Boolean             imageFileMoved=false;
  
  private void createSigImage(String sig){
    try{
      sigObj.setSigCompressionMode(2);			
      sigObj.setDisplayJustifyX(10);
      sigObj.setDisplayJustifyY(10);
      sigObj.setDisplayJustifyMode(5);
      sigObj.setImagePenWidth(2);
      sigObj.setImageXSize(300);
      sigObj.setImageYSize(100);
      sigObj.setImageJustifyX(10);
      sigObj.setImageJustifyY(10);
      sigObj.setImageJustifyMode(5);
      BufferedImage sigImage = sigObj.sigImage();
      int w = sigImage.getWidth(null);
      int h = sigImage.getHeight(null);
      int[] pixels = new int[(w * h) * 2];

      sigImage.setRGB(0, 0, 0, 0, pixels, 0, 0);
      
      try{
        Jimi.putImage(sigImage,".\\sig.jpg");
      }catch(Exception ex){
        //System.out.println("Failed to do jpeg signature stuff");
        ex.printStackTrace();
      }
    
      try{
        String command = "c:\\windows\\system32\\net.exe use Z: \\\\172.16.1.74\\f$";
        Process p = Runtime.getRuntime().exec(command);

        File f=new File(".\\sig.jpg");
        boolean fileExists=f.exists();
        if(!fileExists){
          System.out.println("File Not Found");
        }
        Random rand = new Random(); 
        int randNum = rand.nextInt();        
        Date date=new Date();

        newFileName=new String("Signature_"+date.getTime()+"_"+randNum+".jpg");
        imageFileMoved=f.renameTo(new File("Z:\\Home\\ITS\\sigImages\\"+newFileName));

        if(!imageFileMoved){
          System.out.println("File Not moved");
        }else{
          imageFileMoved=true;
        }

      }catch(SecurityException ex){
        ex.printStackTrace();
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }catch(Exception ex){
      System.out.println("Failed to do signature stuff");
      ex.printStackTrace();
    }
  }
  
  public signaturePadStuff(){
    int      i;
    String   fileName;

    try{
      ClassLoader cl = (com.topaz.sigplus.SigPlus.class).getClassLoader();
      sigObj = (SigPlus)Beans.instantiate(cl, "com.topaz.sigplus.SigPlus");

      setLayout(new GridLayout(1,1));
      add(sigObj);
      pack();
      setTitle("Inventory System - Signature for Equipment Checkout");
      
              
      addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent we){
          //System.exit(0);
          hide();
        }

        public void windowClosed(WindowEvent we){
          //System.exit(0);
          hide();
        }
      });

      sigObj.addSigPlusListener(new SigPlusListener(){
        public void handleTabletTimerEvent(SigPlusEvent0 evt){}

        public void handleNewTabletData(SigPlusEvent0 evt){}

        public void handleKeyPadData(SigPlusEvent0 evt){}
      });

      setSize(640, 256);
      show();
      
      sigObj.setTabletState(0);
      
      sigObj.setTabletModel("SignatureGemLCD4X3New");
      sigObj.setTabletComPort("HID1");
      sigObj.setTabletBaudRate(38400);
      
      //check for pad connectivity
      sigObj.setTabletComTest(true);
      sigObj.setTabletState(1);
      
      if(sigObj.getTabletState()==0){
        sigObj.setTabletComTest(false);
        System.out.println("Cannot locate signature pad...exiting");
        //System.exit(0);
        hide();
        return;
      }
      
      sigObj.setTabletComTest(false);
      sigObj.setLCDCaptureMode(2);

      sigObj.setDisplayRotation(0);
      sigObj.setDisplayJustifyMode(1);
      sigObj.setDisplayJustifyX(50);
      sigObj.setDisplayJustifyY(50);
      sigObj.setTimeStamp("August 16, 2002 13:40:00"); //Timestamp string
      sigObj.setDisplayTimeStamp(true); //Sets up to display the timestamp
      sigObj.setAnnotation("Hill College Inventory System"); //Annotation string
      sigObj.setDisplayAnnotation(true); //Sets up to display annotation

      sigObj.keyPadSetSigWindow(1, 0, 0, 0, 0);
      sigObj.lcdSetWindow(0,0,0,0);

      sigObj.keyPadAddHotSpot(0, 1, 58, 71, 40, 15);
      sigObj.keyPadAddHotSpot(1, 1, 146, 72, 26, 15);
      
      
      MediaTracker mt = new MediaTracker(this);
      rawImages = new Image[numImages];
      String[] imageTitles = {
        "Screen",  //Signature Screen
        "please",  //Please sign before continuing
        "thanks"   //Thanks for signing
      };

      for(i=0;i<numImages;i++){
        fileName = fileBase + imageTitles[i] + ".jpg";
        rawImages[i] = Toolkit.getDefaultToolkit().getImage(fileName);
        mt.addImage(rawImages[i], i+1);
      }

      try{
        mt.waitForAll();
      }catch(Exception e){
        System.out.println("Error opening bitmap files");
      }

      sigObj.lcdRefresh(0, 0, 0, 240, 128);
      sigObj.clearTablet();
      sigObj.keyPadClearSigWindow(1);
      sigObj.lcdWriteImage(0, 2, 0, 0, 240, 92, rawImages[0]);
      sigObj.lcdSetWindow(2, 17, 236, 51);
      sigObj.keyPadSetSigWindow(1, 2, 17, 236, 51);
      
      eventThread = new Thread(this);
      eventThread.start();
    }catch (Exception e){
      e.printStackTrace();
      return;
    }
  }
  
  public void run(){
    try{
      while(true){
        Thread.sleep(300);
        
        if(sigObj.keyPadQueryHotSpot(0)!=0){ /* pressed Clear */          
          sigObj.clearTablet();
          sigObj.keyPadClearSigWindow(1);
          sigObj.lcdWriteImage(0, 2, 0, 0, 240, 92, rawImages[0]);
          sigObj.lcdSetWindow(2, 17, 236, 51);
          sigObj.keyPadSetSigWindow(1, 2, 17, 236, 51);
          
        }else if(sigObj.keyPadQueryHotSpot(1)!=0){ /* Pressed OK */
          
          sigObj.keyPadClearSigWindow(1);
          sigObj.lcdRefresh(1, 186, 53, 35, 15);
          
          if(sigObj.numberOfTabletPoints()>0){
            signature = sigObj.getSigString(); //signature variable gets sig data
            createSigImage(signature);
            sigObj.lcdRefresh(0, 0, 0, 240, 128);
            sigObj.clearTablet(); 
            sigObj.keyPadClearSigWindow(1); 
       			sigObj.lcdWriteImage(0, 2, 3, 46, 233, 24, rawImages[2]);
            Thread.sleep(1000);
            sigObj.setTabletState(1);
            sigObj.lcdRefresh(0, 0, 0, 240, 128);
            sigObj.setLCDCaptureMode(1);
            sigObj.setTabletState(0);
            //System.exit(0);
            hide();
          }else{
            sigObj.clearTablet();
            sigObj.keyPadClearSigWindow(1);
            sigObj.lcdRefresh(0, 0, 0, 240, 128);
            sigObj.lcdWriteImage(0, 2, 0, 0, 234, 21, rawImages[1]);
            Thread.sleep(1000);
            sigObj.clearTablet();
            sigObj.keyPadClearSigWindow(1);
            sigObj.lcdRefresh(0, 0, 0, 240, 128);
            sigObj.lcdWriteImage(0, 2, 0, 0, 240, 92, rawImages[0]);
            sigObj.lcdSetWindow(2, 17, 236, 51);
            sigObj.keyPadSetSigWindow(1, 2, 17, 236, 51);
    		    page = 1;
       			//Start over again
          }
          sigObj.keyPadClearSigWindow(1);
          sigObj.clearTablet();
        }
      }
	  }catch (InterruptedException e){
      e.printStackTrace();
    }
  }
}