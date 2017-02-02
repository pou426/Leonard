package vision;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Ivan Georgiev (s1410984) on 01/02/17.
 * Class to manage Properties I/O
 */
class DetectionPropertiesManager {

    static void loadValues() {
        File saveFile =
                new File("Leonard/vision/calibration/pre_saved_values/calibrationOptions.properties");
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            if (saveFile.exists()) {
                fis = new FileInputStream(saveFile);
                prop.load(fis);

                List<ImageManipulator> pipeline = ImageManipulationPipeline.getInstance().pipeline;
                for (ImageManipulator i : pipeline) {
                    if (i instanceof ImageManipulatorWithOptions)
                        ((ImageManipulatorWithOptions) i).loadModificationSettings(prop);
                }
            }
        } catch (IOException io) {
            System.out.println("No Save File found!");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.out.println("No save file to close.");
                }
            }
        }

    }

    static void saveValues() {
        File saveFile =
                new File("Leonard/vision/calibration/pre_saved_values/calibrationOptions.properties");
        Properties prop = new Properties();
        FileOutputStream fos = null;

        try {
            if (!saveFile.exists())
                saveFile.createNewFile();

            fos = new FileOutputStream(saveFile);

            List<ImageManipulator> pipeline = ImageManipulationPipeline.getInstance().pipeline;
            for (ImageManipulator i : pipeline) {
                if (i instanceof ImageManipulatorWithOptions)
                    ((ImageManipulatorWithOptions) i).saveModificationSettings(prop);
            }

            prop.store(fos, "Detection Data Save File");

        } catch (IOException io) {
            System.out.println("Unable to create a save file!");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println("Unable to close file.");
                }
            }

        }
    }
}