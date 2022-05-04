package nl.multicode.jdl;

import ai.djl.ModelException;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DetectFromImageTest {

    private static Logger log = LoggerFactory.getLogger(DetectFromImageTest.class);

    private DetectFromImage detectFromImage;

    @BeforeEach
    public void setup() {

        detectFromImage = new DetectFromImage();
    }

    @Test
    void detectObjects() throws ModelException, TranslateException, IOException {

        final DetectedObjects detections = detectFromImage.detectObjects(getResourceAbsolutePath("images/dog1.png"));
        assertThat(detections.toJson()).contains("dog");
    }

    private String getResourceAbsolutePath(String resourceName) {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        return file.getAbsolutePath();
    }
}