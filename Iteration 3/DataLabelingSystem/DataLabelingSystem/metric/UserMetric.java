package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@JsonPropertyOrder({"user",
        "number of datasets assigned",
        "datasets with completeness percentages",
        "number of instances labeled",
        "number of unique instances labeled",
        "consistency percentage",
        "average time spent labeling",
        "standard deviation of time spent labeling"})
public class UserMetric {
    private User user;
    @JsonIgnore
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

    public UserMetric(User user) {
        this.user = user;
        this.updateLabelAssignment();
    }

    /*    This method updates the labelAssignments field of this class with using
     LabelAssignments ArrayList from dataset for a specific user.
    */
    public void updateLabelAssignment() {
        ArrayList<LabelAssignment> tempAssignments = new ArrayList<>();
        for (int i = 0; i < user.getAssignedDatasets().size(); i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if (labelAssignment.getUser().getId() == getUser().getId()) {
                    tempAssignments.add(labelAssignment);
                }
            }
        }
        this.labelAssignments = tempAssignments;
    }

    /* A-1 This methods brings the number of assigned datasets for a specific user. */
    @JsonProperty("number of datasets assigned")
    public int getNumberOfDatasets() {
        return user.getAssignedDatasets().size();
    }

    /* A-2 This methods lists all the datasets which a specific user assigned
       with their completeness percentage.
    */
    @JsonProperty("datasets with completeness percentages")
    public HashMap<Dataset,Integer> getDatasetWithCompletenessPercentage(){
        HashMap<Dataset,Integer> completenessPercentage = new HashMap<>();
        ArrayList<Integer> tempInstances = new ArrayList<>();
        int InstanceCounter;

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            int numOfInstanceInDataset = dataset.getInstances().size();
            tempInstances.clear();
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);

                if(labelAssignment.getUser().getId()==getUser().getId()) {

                    if (!tempInstances.contains(labelAssignment.getInstanceId())) {
                        tempInstances.add(labelAssignment.getInstanceId());
                    }
                }
            }

            InstanceCounter = tempInstances.size();
            int compPercentage = (InstanceCounter/numOfInstanceInDataset)*100;
            completenessPercentage.put(dataset,compPercentage);
        }
        return completenessPercentage;
    }

    /* A-3 This method brings total number of instances which are labeled by a specific user. */
    @JsonProperty("number of instances labeled")
    public int getInstancesLabeledCount(){
        int InstanceCounter = 0;

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    InstanceCounter++;
                }
            }
        }
        return InstanceCounter;
    }

    /* A-4 This method brings total number instances which are labeled just one time by a specific user. */
    @JsonProperty("number of unique instances labeled")
    public int getUniqueInstancesLabeledCount(){
        int InstanceCounter;
        ArrayList<Integer> tempInstances = new ArrayList<>();

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    if(!tempInstances.contains(labelAssignment.getInstanceId())){
                        tempInstances.add(labelAssignment.getInstanceId());
                    }
                }
            }
        }
        InstanceCounter = tempInstances.size();
        return InstanceCounter;
    }

    /* A-5 This method calculates labeling consistency of a specific user
       and returns it with percentage form.
    */
    @JsonProperty("consistency percentage")
    public int getConsistencyPercentage() {
        HashMap<Instance, ArrayList<Integer>> tempInstances = new HashMap<>();
        for (int i = 0; i < user.getAssignedDatasets().size(); i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);

                if (labelAssignment.getUser().getId() == getUser().getId()) {

                    if (!tempInstances.containsKey(labelAssignment.getInstance())) {
                        ArrayList<Integer> labelIds = labelAssignment.getLabels()
                                .stream()
                                .map(Label::getId)
                                .collect(Collectors.toCollection(ArrayList::new));
                        tempInstances.put(labelAssignment.getInstance(), labelIds);
                    } else if (tempInstances.containsKey(labelAssignment.getInstance())) {
                        ArrayList<Label> currentLabelsList = labelAssignment.getLabels();

                        for (Label label : currentLabelsList) {
                            tempInstances.get(labelAssignment.getInstance()).add(label.getId());
                        }
                    }
                }
            }
        }
        double labelsMoreThanOne = 0;
        double inconsistent = 0;

        for (HashMap.Entry everySingleInstance : tempInstances.entrySet()) {

            String newLabels = everySingleInstance.getValue().toString().replace("[", "")
                    .replace("]", "").replace(",", "").replace(" ", "");
            int lengthOfNewLabels = everySingleInstance.getValue().toString().replace("[", "")
                    .replace("]", "").replace(",", "").replace(" ", "").length();

            if (lengthOfNewLabels > 1) {
                labelsMoreThanOne++;
                for (int i = 0; i < lengthOfNewLabels; i++) {
                    if (!(newLabels.charAt(0) == newLabels.charAt(i))) {
                        inconsistent++;
                        break;
                    }
                }
            }
        }
        if (labelsMoreThanOne == 0) {
            return 100;

        }
        double consistency = (((labelsMoreThanOne - inconsistent) / labelsMoreThanOne) * 100);
        return (int) consistency;

    }

    @JsonProperty("average time spent labeling")
    /* A-6 This method calculates the average time spent at labeling an instance in seconds */
    public double getAverageLabelTime() {
        double averageTime = 0;
        int labelAssignmentCounter = 0;
        for (int i = 0; i < user.getAssignedDatasets().size(); i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if (labelAssignment.getUser().getId() == getUser().getId()) {
                    averageTime += labelAssignment.getDuration().getSeconds() + (labelAssignment.getDuration().getNano() * Math.pow(10, -9));
                    labelAssignmentCounter++;
                }
            }
        }
        averageTime = averageTime/labelAssignmentCounter;

        return averageTime;
    }

    /* A-7 This method calculates Standard Deviation of time spent at labeling an instance in seconds */
    @JsonProperty("standard deviation of time spent labeling")
    public double getStandardDeviation() {
        double standardDeviation = 0;
        ArrayList<Double> durations = new ArrayList<>();
        for (int i = 0; i < user.getAssignedDatasets().size(); i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if (labelAssignment.getUser().getId() == getUser().getId()) {
                    durations.add(labelAssignment.getDuration().getNano() * Math.pow(10, -9));
                }
            }
        }
        double averageTime = getAverageLabelTime();
        for (int i = 0; i < durations.size(); i++) {
            standardDeviation += (Math.pow((durations.get(i) - averageTime), 2) / (durations.size() - 1));
        }
        standardDeviation = Math.sqrt(standardDeviation);
        return standardDeviation;
    }


    //Getter and Setter methods.
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return labelAssignments;
    }

    public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
        this.labelAssignments = labelAssignments;
    }

}


