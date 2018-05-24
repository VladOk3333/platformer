package com.mygdx.game.Extensions;

import com.mygdx.game.Sprites.MarioClone;
import com.mygdx.game.platformer;
import com.mygdx.game.screens.PlayScreen;
import java.io.File;

public class ModuleEngine {
    
 public static Module _execute = null;
  public static void main(String args[], MarioClone player) {
    String modulePath = args[0];
    System.out.print("Module path: ");
    System.out.println(modulePath);
    /**
     * Создаем загрузчик модулей.
     */
    ModuleLoader loader = new ModuleLoader(modulePath, ClassLoader.getSystemClassLoader());

    /**
     * Получаем список доступных модулей.
     */
   /** File dir = new File(modulePath);
    String[] modules = dir.list();
    if (modules == null)
    {
        System.out.println("Module path does not denote a folder");
        return;
    }*/
   
    /**
     * Загружаем и исполняем каждый модуль.
     */
 
   String module = modulePath;
        if (module.endsWith(".class")) {
            try {
                String moduleName = module.split("modules\\\\")[1];
               moduleName = moduleName.split("\\.class")[0];
                if (moduleName.equals("Module") == false) {
                    System.out.print("Executing loading module: ");
                    System.out.println(moduleName);

                    Class clazz = loader.loadClass( "com.mygdx.game.Extensions.modules." + moduleName);
                    _execute = (Module) clazz.newInstance();
                    _execute.load(player);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    


  }

}
