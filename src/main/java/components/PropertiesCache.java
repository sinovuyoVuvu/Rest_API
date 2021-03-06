package components;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesCache {


   private final Properties configProp = new Properties();
    
   private PropertiesCache()
   {
      //Private constructor to restrict new instances
      System.out.println(this.getClass());
                  
                  InputStream in = this.getClass().getClassLoader().getResourceAsStream("output.properties");
     // InputStream input = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\java\\caUnicentre\\config\\OR.properties");
      System.out.println("Read all properties from file");
      try {
          configProp.load(in);
      } catch (IOException e) {
          e.printStackTrace();
      }
   }

   //Bill Pugh Solution for singleton pattern
   private static class LazyHolder
   {
      private static final PropertiesCache INSTANCE = new PropertiesCache();
   }

   public static PropertiesCache getInstance()
   {
      return LazyHolder.INSTANCE;
   }
    
   public String getProperty(String key){
      return configProp.getProperty(key);
   }
    
   public void setProperty(String key, String value){
                  configProp.setProperty(key, value);
               }
   
   
   public Set<String> getAllPropertyNames(){
      return configProp.stringPropertyNames();
   }
    
   public boolean containsKey(String key){
      return configProp.containsKey(key);
   }
}
