package com.Practice;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String array[] = {"Rakesh","Subhash","Megnath","Teja"};
//        for(int i =0 ;i<array.length;i++)
//        {
//        	System.out.println("Room Members : "+array[i]);
//        }
        for(String members : array)
        {
        	System.out.println("Room Members : "+members);
        }
        
         String var = "desktop cost is 3000$";
        
        final String DEVICE_TYPES[] = {"Desktop", "Mobile"};
        {
        	 for (String deviceType : DEVICE_TYPES) {
        		 
//        		 if(deviceType.equals("Desktop"))
//        		 {
//        			 System.out.println("This is "+var);
//        		 }
//        		 else
//        		 {
//        			 System.out.println("this is mobile");
//        		 }
        		 String res = deviceType.equals("Desktop") ?  "This is "+var : "this is mobile";
        		 System.out.println(res);
        	 }
        	 //String[] resolutions = deviceType.equals("Desktop") ? DESKTOP_RESOLUTIONS : MOBILE_RESOLUTIONS;
        }
        
    }
}
