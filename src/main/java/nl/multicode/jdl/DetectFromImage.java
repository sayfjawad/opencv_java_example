package nl.multicode.jdl;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DetectFromImage {

    private static Logger log = LoggerFactory.getLogger(DetectFromImage.class);

    static {
        OpenCV.loadShared();
    }

    public DetectedObjects detectObjects(String imagePath) throws ModelException, IOException, TranslateException {

        Predictor<Image, DetectedObjects> predictor = loadModel().newPredictor();
        try {
            ImageFactory factory = ImageFactory.getInstance();
            Mat image = Imgcodecs.imread(imagePath);

            final DetectedObjects detectedObjects = predictor.predict(factory.fromImage(image));
            log.info("Detected: {}", detectedObjects.toJson());

            return detectedObjects;
        } finally {
            predictor.close();
        }
    }

    private static ZooModel<Image, DetectedObjects> loadModel() throws IOException, ModelException {

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optApplication(Application.CV.OBJECT_DETECTION)
                        .setTypes(Image.class, DetectedObjects.class)
                        .optFilter("backbone", "mobilenet1.0")
                        .optFilter("dataset", "voc")
                        .optProgress(new ProgressBar())
                        .build();

        return criteria.loadModel();
    }
}