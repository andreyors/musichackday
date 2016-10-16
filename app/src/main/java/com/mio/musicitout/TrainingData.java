package com.mio.musicitout;

import java.util.ArrayList;

public class TrainingData {

    public static String[] TrainingNameArray = {"Core Strength", "30 min ABS", "Cardio Hard" , "HIIT"};

    public static ArrayList<Training> trainingList() {
        ArrayList<Training> list = new ArrayList<>();
        for (int i = 0; i < TrainingNameArray.length; i++) {
            Training Training = new Training();
            Training.name = TrainingNameArray[i];
            Training.imageName = "pic"; // + TrainingNameArray[i].replaceAll("\\s+", "").toLowerCase();
            if (i == 2 || i == 5) {
                Training.isFav = true;
            }
            list.add(Training);
        }

        return (list);
    }

    public static Training getItem(String _id) {
        for (Training Training : trainingList()) {
            if (Training.id.equals(_id)) {
                return Training;
            }
        }

        return null;
    }
}
